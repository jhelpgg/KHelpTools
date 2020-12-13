package khelp.thread.future

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import khelp.thread.TaskContext
import khelp.thread.extensions.unwrap
import khelp.thread.parallel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job

class FutureResult<R : Any> internal constructor(val taskContext: TaskContext)
{
    companion object
    {
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

        fun <R1 : Any, R2 : Any> join(future1: FutureResult<R1>,
                                      future2: FutureResult<R2>): FutureResult<Pair<FutureResult<R1>, FutureResult<R2>>> =
            FutureResult.join(TaskContext.INDEPENDENT, future1, future2)

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

        fun <R1 : Any, R2 : Any, R : Any> combine(future1: FutureResult<R1>,
                                                  future2: FutureResult<R2>,
                                                  combiner: (R1, R2) -> R): FutureResult<R> =
            FutureResult.combine(TaskContext.INDEPENDENT, future1, future2, combiner)

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

    fun status(): FutureResultStatus = this.status.get()

    fun cancel(reason: String)
    {
        if (this.status.compareAndSet(FutureResultStatus.COMPUTING, FutureResultStatus.CANCELED))
        {
            this.cancellationException = CancellationException(reason)
            this.job?.cancel(this.cancellationException)
            this.doCompletion()
        }
    }

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

    fun <R1 : Any> and(context: TaskContext = this.taskContext, task: (R) -> R1): FutureResult<R1>
    {
        val targetFuture = FutureResult<R1>(context)
        FutureAnd(this, task, targetFuture)
        return targetFuture
    }

    fun <R1 : Any> then(context: TaskContext = this.taskContext, task: (FutureResult<R>) -> R1): FutureResult<R1>
    {
        val targetFuture = FutureResult<R1>(context)
        FutureThen(this, task, targetFuture)
        return targetFuture
    }

    fun <R1 : Any> andUnwrap(context: TaskContext = this.taskContext, task: (R) -> FutureResult<R1>): FutureResult<R1>
    {
        val targetFuture = FutureResult<FutureResult<R1>>(context)
        FutureAnd(this, task, targetFuture)
        return targetFuture.unwrap()
    }

    fun <R1 : Any> thenUnwrap(context: TaskContext = this.taskContext,
                              task: (FutureResult<R>) -> FutureResult<R1>): FutureResult<R1>
    {
        val targetFuture = FutureResult<FutureResult<R1>>(context)
        FutureThen(this, task, targetFuture)
        return targetFuture.unwrap()
    }

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
