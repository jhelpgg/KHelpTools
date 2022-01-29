package khelp.thread.weak

import khelp.thread.delay
import khelp.thread.future.FutureResult
import khelp.utilities.log.debug
import java.lang.ref.WeakReference
import kotlin.math.max

class InstanceTimeout<T>(instance : T, timeoutMilliseconds : Int)
{
    private var instance : T? = instance
    private val timeoutMilliseconds = max(1, timeoutMilliseconds)
    private var timeoutFuture : FutureResult<Unit>? = null

    init
    {
        this()
    }

    operator fun invoke() : T? =
        synchronized(this)
        {
            if (this.instance != null)
            {
                this.timeoutFuture?.cancel("Reinitialize delay")
                this.timeoutFuture = delay(this.timeoutMilliseconds) {
                    synchronized(this) {
                        this.instance = null
                        this.timeoutFuture = null
                    }
                }
            }

            this.instance
        }
}