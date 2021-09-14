package khelp.thread.observable

import khelp.thread.TaskContext
import java.util.concurrent.atomic.AtomicInteger

class Observer<T:Any> internal constructor(private val observable : Observable<T>,
                                    internal val taskContext : TaskContext, internal val action : (T) -> Unit)
{
    companion object
    {
        private val NEXT_ID = AtomicInteger(0)
    }

    internal val id : Int = Observer.NEXT_ID.getAndIncrement()

    fun stopObserve()
    {
        this.observable.stopObserve(this.id)
    }

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is Observer<*>)
        {
            return false
        }

        return this.id == other.id
    }

    override fun hashCode() : Int = this.id
}
