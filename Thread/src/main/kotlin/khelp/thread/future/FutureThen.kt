package khelp.thread.future

import kotlinx.coroutines.CancellationException

internal class FutureThen<R : Any, R1 : Any>(private val sourceFuture: FutureResult<R>, private val task: (FutureResult<R>) -> R1, private val targetFuture: FutureResult<R1>) : FutureResultCallback<R>
{
    init
    {
        this.sourceFuture.registerCallback(this)
        this.targetFuture.registerCallback(object : FutureResultCallback<R1>
                                           {
                                               override fun onResult(result: R1) = Unit

                                               override fun onCancel(cancellationException: CancellationException)
                                               {
                                                   this@FutureThen.sourceFuture.cancel(cancellationException.message
                                                                                       ?: "Cancelled")
                                               }
                                           })
    }

    override fun onResult(result: R)
    {
        this.doCompletion()
    }

    override fun onFailure(exception: Throwable)
    {
        this.doCompletion()
    }

    override fun onCancel(cancellationException: CancellationException)
    {
        this.doCompletion()
    }

    private fun doCompletion()
    {
        try
        {
            this.targetFuture.result(this.task(this.sourceFuture))
        }
        catch (cancellationException: CancellationException)
        {
            this.targetFuture.cancel(cancellationException.message ?: "Cancelled")
        }
        catch (throwable: Throwable)
        {
            this.targetFuture.completion(throwable)
        }
    }
}
