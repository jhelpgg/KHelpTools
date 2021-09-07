package khelp.thread.flow

import khelp.thread.Mutex
import khelp.thread.TaskContext
import java.util.concurrent.atomic.AtomicBoolean

class FlowJoin<T1 : Any, T2 : Any, R>(flow1 : Flow<T1>, flow2 : Flow<T2>,
                                      taskContext : TaskContext, private val combiner : (T1, T2) -> R)
{
    private lateinit var result1 : T1
    private lateinit var result2 : T2
    private val result1Received = AtomicBoolean(false)
    private val result2Received = AtomicBoolean(false)
    private val flowData = FlowData<R>()
    private val mutex = Mutex()
    val flow : Flow<R> = this.flowData.flow

    init
    {
        flow1.then(taskContext) { result1 ->
            this.mutex {
                this.result1 = result1
                this.result1Received.set(true)
                this.combine()
            }
        }

        flow2.then(taskContext) { result2 ->
            this.mutex {
                this.result2 = result2
                this.result2Received.set(true)
                this.combine()
            }
        }
    }

    private fun combine()
    {
        if (this.result1Received.get() && this.result2Received.get())
        {
            this.flowData.publish(this.combiner(this.result1, this.result2))
        }
    }
}
