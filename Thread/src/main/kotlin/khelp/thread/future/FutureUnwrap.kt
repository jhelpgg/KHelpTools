package khelp.thread.future

import kotlinx.coroutines.CancellationException

internal class FutureUnwrap<R : Any>(private val sourceFuture: FutureResult<FutureResult<R>>, private val targetFuture: FutureResult<R>) : FutureResultCallback<FutureResult<R>>
{
    init
    {
        this.sourceFuture.registerCallback(this)
        this.targetFuture.registerCallback(object : FutureResultCallback<R>
                                           {
                                               override fun onResult(result: R) = Unit

                                               override fun onCancel(cancellationException: CancellationException)
                                               {
                                                   this@FutureUnwrap.sourceFuture.cancel(cancellationException.message
                                                                                         ?: "Cancelled")
                                               }
                                           })
    }

    override fun onResult(result: FutureResult<R>)
    {
        result.registerCallback(this@FutureUnwrap.targetFuture::result)
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