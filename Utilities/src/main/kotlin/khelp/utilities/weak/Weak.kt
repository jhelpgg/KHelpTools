package khelp.utilities.weak

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

class Weak<T>(value : T? = null)
{
    private var reference : WeakReference<T> = WeakReference<T>(value)

    operator fun getValue(thisRef : Any, property : KProperty<*>) : T? = this.reference.get()

    operator fun setValue(thisRef : Any, property : KProperty<*>, value : T?)
    {
        this.reference = WeakReference(value)
    }
}