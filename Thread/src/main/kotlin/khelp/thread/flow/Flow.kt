package khelp.thread.flow

import khelp.thread.TaskContext
import khelp.utilities.collections.IntMap

class Flow<T> internal constructor()
{
    private val elements = IntMap<FlowElement<T>>()
    private var flowParent : Flow<*>? = null
    private var idInParent = - 1

    fun <R> then(taskContext : TaskContext, action : (T) -> R) : Flow<R>
    {
        val flowData = FlowData<R>()
        val flow = flowData.flow
        val flowElement = FlowElement<T>(taskContext) { value -> flowData.publish(action(value)) }
        flow.flowParent = this
        flow.idInParent = flowElement.id
        synchronized(this.elements) { this.elements[flowElement.id] = flowElement }
        return flow
    }

    fun cancel()
    {
        this.flowParent?.cancel(this.idInParent)
        this.flowParent = null
        this.idInParent = - 1
    }

    internal fun publish(value : T)
    {
        synchronized(this.elements)
        {
            for (element in this.elements.values())
            {
                element.taskContext.parallel(value, element.action)
            }
        }
    }

    private fun cancel(id : Int)
    {
        synchronized(this.elements)
        {
            this.elements.remove(id)
        }
    }
}
