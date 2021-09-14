package khelp.utilities.collections

import kotlin.math.max


/**
 * Cache of elements
 *
 * It creates elements if not already computed, remove some if not enough place and recreate them later.
 * @param maximumSize Maximum size of cache
 * @param valueCreator Function for create value from a key
 * @param K Cache key type
 * @param V Cache value type
 */
class Cache<in K, out V>(maximumSize : Int, private val valueCreator : (K) -> V)
{
    val maximumSize : Int = max(8, maximumSize)

    private val cache = HashMap<K, CacheElement<V>>(this.maximumSize)

    /**
     * Obtain a cache element
     * @param key Cache key
     * @return Value store in cache (May be just created)
     */
    operator fun get(key : K) : V
    {
        var element = this.cache[key]

        if (element != null)
        {
            element.lastTime = System.currentTimeMillis()
            return element.value
        }

        element = CacheElement(System.currentTimeMillis(), this.valueCreator(key))

        if (this.cache.size >= this.maximumSize)
        {
            var time = Long.MAX_VALUE
            var toRemove : K? = null

            for ((keyCache, value) in this.cache)
            {
                if (value.lastTime < time)
                {
                    toRemove = keyCache
                    time = value.lastTime
                }
            }

            if (toRemove != null)
            {
                this.cache.remove(toRemove as K)
            }
        }

        this.cache[key] = element
        return element.value
    }
}
