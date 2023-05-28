@file:Suppress("UNCHECKED_CAST")

package khelp.utilities.thread.delegate

import java.lang.reflect.Proxy

fun <I> threadSafe(interfaceClass : Class<I>, instance : I) : I
{
    if (! interfaceClass.isInterface)
    {
        throw IllegalArgumentException("interfaceClass must represents an interface")
    }

    return Proxy.newProxyInstance(interfaceClass.classLoader, arrayOf(interfaceClass),
                                  DelegateThreadSafeHandler(instance)) as I
}

inline fun <reified I> threadSafe(instance : I) = ThreadSafe<I>(I::class.java, instance)
