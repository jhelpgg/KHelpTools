package khelp.thread.extensions

import khelp.thread.TaskContext
import khelp.thread.future.FutureResult

/**
 * Create a future with an already known result
 */
fun <R : Any> Exception.futureFail() : FutureResult<R>
{
    val future = FutureResult<R>(TaskContext.INDEPENDENT)
    future.completion(this)
    return future
}
