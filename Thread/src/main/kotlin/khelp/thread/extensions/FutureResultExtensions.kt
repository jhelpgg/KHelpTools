package khelp.thread.extensions

import khelp.thread.future.FutureResult
import khelp.thread.future.FutureUnwrap

fun <R : Any> FutureResult<FutureResult<R>>.unwrap(): FutureResult<R>
{
    val unwrapped = FutureResult<R>(this.taskContext)
    FutureUnwrap(this, unwrapped)
    return unwrapped
}
