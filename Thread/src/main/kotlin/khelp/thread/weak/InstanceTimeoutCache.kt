package khelp.thread.weak

import kotlin.math.max

class InstanceTimeoutCache<K, V>(timeoutMilliseconds : Int, private val provider : (K) -> V)
{
    private val timeoutMilliseconds = max(1, timeoutMilliseconds)
    private val cache = HashMap<K, InstanceTimeout<V>>()

    operator fun get(key : K) : V
    {
        val instanceTimeout = this.cache.getOrPut(key) { InstanceTimeout(this.provider(key), this.timeoutMilliseconds) }
        var result = instanceTimeout()

        if (result == null)
        {
            result = this.provider(key)
            this.cache[key] = InstanceTimeout(result, this.timeoutMilliseconds)
        }

        return result !!
    }
}