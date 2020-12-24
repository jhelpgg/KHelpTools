package khelp.thread.extensions

import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.parallel

/**
 * Launch task on each element on given context
 */
fun <T : Any> Iterator<T>.forEachParallel(context: TaskContext = TaskContext.INDEPENDENT,
                                          action: (T) -> Unit): FutureResult<Unit>
{
    val futures = ArrayList<FutureResult<*>>()

    while (this.hasNext())
    {
        futures.add(parallel(context, this.next(), action))
    }

    return FutureResult.joinAll(futures)
}
