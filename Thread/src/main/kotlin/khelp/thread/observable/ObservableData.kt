package khelp.thread.observable

class ObservableData<T : Any>(private var value : T)
{
    private val lock = Object()

    val observable = Observable<T>(this)

    fun value() : T = synchronized(this.lock) { this.value }

    fun value(value : T)
    {
        synchronized(this.lock) { this.value = value }
        this.observable.valueChanged()
    }
}
