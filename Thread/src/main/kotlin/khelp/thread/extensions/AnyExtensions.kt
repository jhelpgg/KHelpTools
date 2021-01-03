package khelp.thread.extensions

import khelp.thread.TaskContext
import khelp.thread.future.FutureResult

/**
 * Create a future with an already known result
 */
val <T : Any> T.futureResult: FutureResult<T>
    get()
    {
        val future = FutureResult<T>(TaskContext.INDEPENDENT)
        future.result(this)
        return future
    }
