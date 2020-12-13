package khelp.thread.future

import khelp.thread.parallel
import kotlinx.coroutines.CancellationException

internal class FutureAnd<R : Any, R1 : Any>(private val sourceFuture: FutureResult<R>, private val task: (R) -> R1, private val targetFuture: FutureResult<R1>) : FutureResultCallback<R>
{
    init
    {
        this.sourceFuture.registerCallback(this)
        this.targetFuture.registerCallback(object : FutureResultCallback<R1>
                                           {
                                               override fun onResult(result: R1) = Unit

                                               override fun onCancel(cancellationException: CancellationException)
                                               {
                                                   this@FutureAnd.sourceFuture.cancel(cancellationException.message
                                                                                      ?: "Cancelled")
                                               }
                                           })
    }

    override fun onResult(result: R)
    {
        if (this.sourceFuture.taskContext == this.targetFuture.taskContext)
        {
            this.playTask(result)
        }
        else
        {
            parallel(this.targetFuture.taskContext, result, this::playTask)
        }
    }

    private fun playTask(result: R)
    {
        try
        {
            this.targetFuture.result(this.task(result))
        }
        catch (cancellationException: CancellationException)
        {
            this.onCancel(cancellationException)
        }
        catch (throwable: Throwable)
        {
            this.onFailure(throwable)
        }
    }

    override fun onFailure(exception: Throwable)
    {
        this.targetFuture.completion(exception)
    }

    override fun onCancel(cancellationException: CancellationException)
    {
        this.targetFuture.cancel(cancellationException.message ?: "Cancelled")
    }
}