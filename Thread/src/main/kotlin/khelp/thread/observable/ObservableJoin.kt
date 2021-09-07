package khelp.thread.observable

import khelp.thread.Mutex
import khelp.thread.TaskContext
import khelp.thread.future.FutureResult

class ObservableJoin<T1 : Any, T2 : Any, R : Any>(private val observable1 : Observable<T1>,
                                                  private val observable2 : Observable<T2>,
                                                  private val taskContext : TaskContext,
                                                  private val combiner : (T1, T2) -> R)
{
    private val observableDataFuture =
        this.taskContext.parallel {
            ObservableData<R>(this.combiner(this.observable1.value(), this.observable2.value()))
        }
    private val mutex = Mutex()
    val observableFuture : FutureResult<Observable<R>> =
        this.observableDataFuture.and { observableData -> observableData.observable }

    init
    {
        this.observable1.observedBy(TaskContext.INDEPENDENT) { this.combine() }
        this.observable2.observedBy(TaskContext.INDEPENDENT) { this.combine() }
    }

    private fun combine()
    {
        this.mutex {
            val result1 : T1 = this.observable1.value()
            val result2 : T2 = this.observable2.value()
            this.observableDataFuture.and(this.taskContext) { observableData ->
                observableData.value(this.combiner(result1, result2))
            }
        }
    }
}
