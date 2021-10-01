package khelp.thread.observable

import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.parallel
import khelp.utilities.collections.IntMap

class Observable<T : Any> internal constructor(private val observableData : ObservableData<T>)
{
    private val observers = IntMap<Observer<T>>()
    private var internalObserver : Observer<*>? = null

    fun value() : T = this.observableData.value()

    fun <R : Any> then(taskContext : TaskContext, action : (T) -> R) : FutureResult<Observable<R>> =
        parallel(taskContext, this.value(), action)
            .and { result ->
                val observableData = ObservableData<R>(result)
                val observer = this.addObserver(taskContext) { value -> observableData.value(action(value)) }
                val observable = observableData.observable
                observable.internalObserver = observer
                observable
            }

    fun observedBy(taskContext : TaskContext, action : (T) -> Unit) : Observer<T>
    {
        val value = this.value()
        taskContext.parallel(TaskContext.INDEPENDENT) { action(value) }
        return this.addObserver(taskContext) { actualValue -> action(actualValue) }
    }

    fun observedBy(taskContext : TaskContext, condition : (T) -> Boolean, action : (T) -> Unit) : Observer<T>
    {
        val value = this.value()
        taskContext.parallel(TaskContext.INDEPENDENT) {
            if (condition(value))
            {
                action(value)
            }
        }
        return this.addObserver(taskContext) { actualValue ->
            if (condition(actualValue))
            {
                action(actualValue)
            }
        }
    }

    internal fun valueChanged(value : T)
    {
        synchronized(this.observers)
        {
            for (observer in this.observers.values())
            {
                observer.taskContext.parallel(value, observer.action)
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

    private fun addObserver(taskContext : TaskContext, action : (T) -> Unit) : Observer<T>
    {
        val observer = Observer(this, taskContext, action)

        synchronized(this.observers) { this.observers[observer.id] = observer }

        return observer
    }
}
