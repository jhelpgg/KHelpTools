package khelp.thread.flow

import khelp.thread.TaskContext
import java.util.concurrent.atomic.AtomicInteger

internal class FlowElement<T>(val taskContext : TaskContext, val action : (T) -> Unit)
{
    companion object
    {
        private val NEXT_ID = AtomicInteger(0)
    }

    val id : Int = FlowElement.NEXT_ID.getAndIncrement()
}