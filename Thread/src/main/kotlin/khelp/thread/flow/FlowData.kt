package khelp.thread.flow

class FlowData<T>
{
    val flow = Flow<T>()

    fun publish(value : T)
    {
        this.flow.publish(value)
    }
}
