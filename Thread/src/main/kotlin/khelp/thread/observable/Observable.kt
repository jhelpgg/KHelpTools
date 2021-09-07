package khelp.thread.observable

import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.parallel
import khelp.utilities.collections.IntMap

class Observable<T : Any> internal constructor(private val observableData : ObservableData<T>)
{
    private val observers = IntMap<Observer>()
    private var internalObserver : Observer? = null

    fun value() : T = this.observableData.value()

    fun <R : Any> then(taskContext : TaskContext, action : (T) -> R) : FutureResult<Observable<R>> =
        parallel(taskContext, this.value(), action)
            .and { result ->
                val observableData = ObservableData<R>(result)
                val observer = this.addObserver(taskContext) { observableData.value(action(this.value())) }
                val observable = observableData.observable
                observable.internalObserver = observer
                observable
            }

    fun observedBy(taskContext : TaskContext, action : (T) -> Unit) : Observer
    {
        taskContext.parallel { action(this.value()) }
        return this.addObserver(taskContext) { action(this.value()) }
    }

    internal fun valueChanged()
    {
        synchronized(this.observers)
        {
            for (observer in this.observers.values())
            {
                observer.taskContext.parallel(observer.action)
            }
        }
    }

    fun stopObserve()
    {
        this.internalObserver?.stopObserve()
        this.internalObserver = null
    }

    internal fun stopObserve(id : Int)
    {
        synchronized(this.observers) { this.observers.remove(id) }
    }

    private fun addObserver(taskContext : TaskContext, action : () -> Unit) : Observer
    {
        val observer = Observer(this, taskContext, action)

        synchronized(this.observers) { this.observers[observer.id] = observer }

        return observer
    }
}
