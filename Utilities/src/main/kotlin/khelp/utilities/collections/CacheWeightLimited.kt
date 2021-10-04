package khelp.utilities.collections

import khelp.utilities.log.warning
import kotlin.math.max

class CacheWeightLimited<in K, out V>(maximumWeight : Long,
                                      private val creator : (K) -> Pair<V, Long>)
{
    val maximumWeight = max(1024L, maximumWeight)
    private var currentWeight = 0L
    private val cache = HashMap<K, CacheWeightLimitedElement<V>>()

    operator fun get(key : K) : V
    {
        val element = this.cache[key]

        if (element != null)
        {
            element.lastTime = System.currentTimeMillis()
            return element.element
        }

        val (value, weight) = this.creator(key)

        // If too heavy, can't be stored
        if (weight > this.maximumWeight)
        {
            return value
        }

        while (this.currentWeight + weight > this.maximumWeight)
        {
            var time = Long.MAX_VALUE
            var keyToRemove : K? = null
            var elementToRemove : CacheWeightLimitedElement<V>? = null

            for ((keyTested, elementTested) in this.cache)
            {
                if (elementTested.lastTime < time)
                {
                    time = elementTested.lastTime
                    keyToRemove = keyTested
                    elementToRemove = elementTested
                }
            }

            if (keyToRemove == null || elementToRemove == null)
            {
                warning("Should never goes here, we must have found one element")
                break
            }

            this.cache.remove(keyToRemove)
            this.currentWeight -= elementToRemove.weight
        }

        this.currentWeight += weight
        this.cache[key] = CacheWeightLimitedElement(System.currentTimeMillis(), weight, value)
        return value
    }
}