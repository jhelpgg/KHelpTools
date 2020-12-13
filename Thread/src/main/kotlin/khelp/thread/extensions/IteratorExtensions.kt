package khelp.thread.extensions

import khelp.thread.TaskContext
import khelp.thread.parallel

fun <T : Any> Iterator<T>.forEachParallel(context: TaskContext = TaskContext.INDEPENDENT, action: (T) -> Unit)
{
    this.forEach { element -> parallel(context, element, action) }
}
