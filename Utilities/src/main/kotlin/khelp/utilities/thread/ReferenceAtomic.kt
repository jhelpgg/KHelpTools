package khelp.utilities.thread

import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty

class ReferenceAtomic<T>(initialValue : T)
{
    private val atomic = AtomicReference<T>(initialValue)

    operator fun getValue(thisRef : Any, property : KProperty<*>) : T = this.atomic.get()

    operator fun setValue(thisRef : Any, property : KProperty<*>, value : T)
    {
        this.atomic.set(value)
    }
}
