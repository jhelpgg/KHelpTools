package khelp.thread.observable

import khelp.thread.Mutex

class ObservableData<T : Any>(private var value : T)
{
    private val mutex = Mutex()

    val observable = Observable<T>(this)

    fun value() : T = this.mutex { this.value }

    fun value(value : T)
    {
        this.mutex {
            this.value = value
            this.observable.valueChanged(value)
        }
    }

    fun valueIf(value : T, condition : (T) -> Boolean) : Boolean
    {
        var changed = false

        this.mutex {
            if (condition(this.value))
            {
                this.value = value
                this.observable.valueChanged(value)
                changed = true
            }
        }

        return changed
    }
}
