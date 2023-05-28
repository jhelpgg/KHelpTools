package khelp.utilities.thread.delegate

import khelp.utilities.reflect.isVoid
import khelp.utilities.thread.Mutex
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

internal class DelegateThreadSafeHandler<I>(private val instance : I) : InvocationHandler
{
    private val mutex = Mutex()

    @Suppress("NAME_SHADOWING")
    override fun invoke(proxy : Any?, method : Method?, args : Array<out Any>?) : Any
    {
        val method = method !!

        val result =
            this.mutex.playInCriticalSection {
                if (args == null)
                {
                    method.invoke(this.instance)
                }
                else
                {
                    method.invoke(this.instance, *args)
                }
            }

        if (method.returnType.isVoid)
        {
            return Unit
        }

        return result
    }

}
