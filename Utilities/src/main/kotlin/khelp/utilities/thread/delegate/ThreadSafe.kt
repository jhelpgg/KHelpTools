package khelp.utilities.thread.delegate

import kotlin.reflect.KProperty

class ThreadSafe<I>(interfaceClass : Class<I>, implementation : I)
{
    private val dedicated : I = threadSafe(interfaceClass, implementation)

    operator fun getValue(thisRef : Any, property : KProperty<*>) : I = this.dedicated
}
