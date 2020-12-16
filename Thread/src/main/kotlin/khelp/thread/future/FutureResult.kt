package khelp.thread.future

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import khelp.thread.TaskContext
import khelp.thread.extensions.unwrap
import khelp.thread.parallel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job

/**
 * Future result of an operation/task.
 *
 * The future result is said complete when result is computed, or computation failed or cancelled.
 *
 * Il is possible to link on each future complemetion status.
 *
 * It also possible to program some reaction when completion is known.
 *
 * For example, it it is possible to say :
 * When **A** complete in success, use the result to do **B***, then when **B*** succeed do **C*** and so on.
 */
class FutureResult<R : Any> internal constructor(val taskContext: TaskContext)
{
    companion object
    {
        /**
         * Create a future result that completes when all given futures completes
         */
        fun joinAll(vararg futures: FutureResult<*>): FutureResult<Unit>
        {
            val futureJoinAll = FutureResult<Unit>(TaskContext.INDEPENDENT)

            when (futures.size)
            {
                0    -> futureJoinAll.result(Unit)
                1    -> futures[0].then { futureJoinAll.result(Unit) }
                else ->
                {
                    val left = AtomicInteger(futures.size)
                    val action: () -> Unit = {
                        if (left.decrementAndGet() == 0)
                        {
                            futureJoinAll.result(Unit)
                        }
                    }

                    for (future in futures)
                    {
                        future.then { action() }
                    }
                }
            }

            return futureJoinAll
        }

        /**
         * Create future result that completes when both given futures completes.
         *
         * The result future will have the futures as result to be able know their status and potential result
         */
        fun <R1 : Any, R2 : Any> join(future1: FutureResult<R1>,
                                      future2: FutureResult<R2>): FutureResult<Pair<FutureResult<R1>, FutureResult<R2>>> =
            FutureResult.join(TaskContext.INDEPENDENT, future1, future2)

        /**
         * Create future result that completes when both given futures completes.
         *
         * The result future will have the futures as result to be able know their status and potential result
         */
        fun <R1 : Any, R2 : Any> join(context: TaskContext,
                                      future1: FutureResult<R1>,
                                      future2: FutureResult<R2>): FutureResult<Pair<FutureResult<R1>, FutureResult<R2>>>
        {
            val numberLeft = AtomicInteger(2)
            val futureJoin = FutureResult<Pair<FutureResult<R1>, FutureResult<R2>>>(context)
            val task: (FutureResult<*>) -> Unit = { _ ->
                if (numberLeft.decrementAndGet() == 0)
                {
                    futureJoin.result(Pair(future1, future2))
                }
            }

            future1.then(context, task)
            future2.then(context, task)

            return futureJoin
        }

        /**
         * Combine the result on given future in a third one
         */
        fun <R1 : Any, R2 : Any, R : Any> combine(future1: FutureResult<R1>,
                                                  future2: FutureResult<R2>,
                                                  combiner: (R1, R2) -> R): FutureResult<R> =
            FutureResult.combine(TaskContext.INDEPENDENT, future1, future2, combiner)

        /**
         * Combine the result on given future in a third one
         */
        fun <R1 : Any, R2 : Any, R : Any> combine(context: TaskContext,
                                                  future1: FutureResult<R1>,
                                                  future2: FutureResult<R2>,
                                                  combiner: (R1, R2) -> R): FutureResult<R> =
            FutureResult.join(context, future1, future2)
                .and { (f1, f2) ->
                    val status1 = f1.status()
                    val status2 = f2.status()

                    if (status1 == FutureResultStatus.CANCELED)
                    {
                        var exception: CancellationException? = null
                        f1.onCancel { exception = it }
                        throw exception!!
                    }

                    if (status2 == FutureResultStatus.CANCELED)
                    {
                        var exception: CancellationException? = null
                        f2.onCancel { exception = it }
                        throw exception!!
                    }

                    if (status1 == FutureResultStatus.FAILED)
                    {
                        var exception: Throwable? = null
                        f1.onFailure { exception = it }
                        throw exception!!
                    }

                    if (status2 == FutureResultStatus.FAILED)
                    {
                        var exception: Throwable? = null
                        f2.onFailure { exception = it }
                        throw exception!!
                    }

                    var result1: R1? = null
                    f1.onResult { result1 = it }
                    var result2: R2? = null
                    f2.onResult { result2 = it }
                    combiner(result1!!, result2!!)
                }
    }

    private val status = AtomicReference<FutureResultStatus>(FutureResultStatus.COMPUTING)

    private lateinit var result: R
    private lateinit var cancellationException: CancellationException
    private lateinit var failureException: Throwable
    private var job: Job? = null
    private val futureResultCallbacks = ArrayList<FutureResultCallback<R>>()
    private val lock = Object()

    /**
     * Current future status.
     *
     * It is indicative, often use when future complete to know if succeed or not.
     *
     * Don't loop on it, prefer use one other methods to wait completion done
     */
    fun status(): FutureResultStatus = this.status.get()

    /**
     * Cancel the task.
     *
     * Does nothing if future already completed
     */
    fun cancel(reason: String)
    {
        if (this.status.compareAndSet(FutureResultStatus.COMPUTING, FutureResultStatus.CANCELED))
        {
            this.cancellationException = CancellationException(reason)
            this.job?.cancel(this.cancellationException)
            this.doCompletion()
        }
    }

    /**
     * Register call back when completion is done.
     *
     * If future already complete, the callback is immediately called.
     */
    fun registerCallback(futureResultCallback: FutureResultCallback<R>)
    {
        when (this.status.get())
        {
            FutureResultStatus.COMPUTING -> this.futureResultCallbacks.add(futureResultCallback)
            FutureResultStatus.SUCCEED   -> futureResultCallback.onResult(this.result)
            FutureResultStatus.FAILED    -> futureResultCallback.onFailure(this.failureException)
            FutureResultStatus.CANCELED  -> futureResultCallback.onCancel(this.cancellationException)
            else                         -> Unit
        }
    }

    /**
     * Launch a task when the future complete with success. The task will receive the future result.
     *
     * The task is only called on success.
     *
     * @return Future on the combination of this future followed by given task
     */
    fun <R1 : Any> and(context: TaskContext = this.taskContext, task: (R) -> R1): FutureResult<R1>
    {
        val targetFuture = FutureResult<R1>(context)
        FutureAnd(this, task, targetFuture)
        return targetFuture
    }

    /**
     * Launch a task when the future complete.
     *
     * @return Future on the combination of this future followed by given task
     */
    fun <R1 : Any> then(context: TaskContext = this.taskContext, task: (FutureResult<R>) -> R1): FutureResult<R1>
    {
        val targetFuture = FutureResult<R1>(context)
        FutureThen(this, task, targetFuture)
        return targetFuture
    }

    /**
     * Launch a task when the future complete with success. The task will receive the future result.
     *
     * The task is only called on success.
     *
     * @return Future on the combination of this future followed by given task
     */
    fun <R1 : Any> andUnwrap(context: TaskContext = this.taskContext, task: (R) -> FutureResult<R1>): FutureResult<R1>
    {
        val targetFuture = FutureResult<FutureResult<R1>>(context)
        FutureAnd(this, task, targetFuture)
        return targetFuture.unwrap()
    }

    /**
     * Launch a task when the future complete.
     *
     * @return Future on the combination of this future followed by given task
     */
    fun <R1 : Any> thenUnwrap(context: TaskContext = this.taskContext,
                              task: (FutureResult<R>) -> FutureResult<R1>): FutureResult<R1>
    {
        val targetFuture = FutureResult<FutureResult<R1>>(context)
        FutureThen(this, task, targetFuture)
        return targetFuture.unwrap()
    }

    /**
     * Register a callback if completion succeed.
     */
    fun onResult(context: TaskContext = this.taskContext, task: (R) -> Unit)
    {
        this.registerCallback { result ->
            if (this.taskContext == context)
            {
                task(result)
            }
            else
            {
                parallel(context, result, task)
            }
        }
    }

    /**
     * Register a callback if completion failed.
     */
    fun onFailure(context: TaskContext = this.taskContext, task: (Throwable) -> Unit)
    {
        this.registerCallback(object : FutureResultCallback<R>
                              {
                                  override fun onResult(result: R) = Unit

                                  override fun onFailure(exception: Throwable)
                                  {
                                      if (context == this@FutureResult.taskContext)
                                      {
                                          task(exception)
                                      }
                                      else
                                      {
                                          parallel(context, exception, task)
                                      }
                                  }
                              })
    }

    /**
     * Register a callback if completion canceled.
     */
    fun onCancel(context: TaskContext = this.taskContext, task: (CancellationException) -> Unit)
    {
        this.registerCallback(object : FutureResultCallback<R>
                              {
                                  override fun onResult(result: R) = Unit

                                  override fun onCancel(cancellationException: CancellationException)
                                  {
                                      if (context == this@FutureResult.taskContext)
                                      {
                                          task(cancellationException)
                                      }
                                      else
                                      {
                                          parallel(context, cancellationException, task)
                                      }
                                  }
                              })
    }

    /**
     * Block current thread until future completes
     */
    fun waitCompletion()
    {
        synchronized(this.lock)
        {
            while (this.status.get() == FutureResultStatus.COMPUTING)
            {
                try
                {
                    this.lock.wait()
                }
                catch (_: Exception)
                {
                }
            }
        }
    }

    internal fun job(job: Job)
    {
        this.job = job
        job.invokeOnCompletion(this::completion)
    }

    internal fun result(result: R)
    {
        if (this.status.compareAndSet(FutureResultStatus.COMPUTING, FutureResultStatus.SUCCEED))
        {
            this.result = result
            this.doCompletion()
        }
    }

    internal fun completion(cause: Throwable?)
    {
        if (cause != null && cause !is CancellationException)
        {
            if (this.status.compareAndSet(FutureResultStatus.COMPUTING, FutureResultStatus.FAILED))
            {
                this.failureException = cause
                this.doCompletion()
            }
        }
    }

    private fun doCompletion()
    {
        when (this.status.get())
        {
            FutureResultStatus.SUCCEED  ->
                for (futureResultCallback in this.futureResultCallbacks)
                {
                    futureResultCallback.onResult(this.result)
                }
            FutureResultStatus.FAILED   ->
                for (futureResultCallback in this.futureResultCallbacks)
                {
                    futureResultCallback.onFailure(this.failureException)
                }
            FutureResultStatus.CANCELED ->
                for (futureResultCallback in this.futureResultCallbacks)
                {
                    futureResultCallback.onCancel(this.cancellationException)
                }
            else                        -> Unit
        }

        this.futureResultCallbacks.clear()
        this.job = null

        synchronized(this.lock)
        {
            this.lock.notifyAll()
        }
    }
}
