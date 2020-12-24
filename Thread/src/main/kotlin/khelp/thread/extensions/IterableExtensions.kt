package khelp.thread.extensions

import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.parallel

/**
 * Launch task on each element on given context
 */
fun <T : Any> Iterable<T>.forEachParallel(context: TaskContext = TaskContext.INDEPENDENT,
                                          action: (T) -> Unit): FutureResult<Unit>
{
    val futures = ArrayList<FutureResult<*>>()

    for (element in this)
    {
        futures.add(parallel(context, element, action))
    }

    return FutureResult.joinAll(futures)
}
