package khelp.thread.extensions

import khelp.thread.TaskContext
import khelp.thread.future.FutureResult

val <T : Any> T.futureResult: FutureResult<T>
    get()
    {
        val future = FutureResult<T>(TaskContext.INDEPENDENT)
        future.result(this)
        return future
    }
