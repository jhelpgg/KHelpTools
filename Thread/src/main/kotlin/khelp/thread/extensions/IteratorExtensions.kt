package khelp.thread.extensions

import khelp.thread.TaskContext
import khelp.thread.parallel

/**
 * Launch task on each element on given context
 */
fun <T : Any> Iterator<T>.forEachParallel(context: TaskContext = TaskContext.INDEPENDENT, action: (T) -> Unit)
{
    this.forEach { element -> parallel(context, element, action) }
}
