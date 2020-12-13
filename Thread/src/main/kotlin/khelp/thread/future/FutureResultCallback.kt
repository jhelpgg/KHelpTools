package khelp.thread.future

import kotlinx.coroutines.CancellationException


fun interface FutureResultCallback<R : Any> {
    fun onResult(result: R)

    fun onFailure(exception: Throwable) = Unit

    fun onCancel(cancellationException: CancellationException) = Unit
}