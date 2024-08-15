package khelp.utilities.interval

import java.util.Objects
import khelp.utilities.interval.limit.Infinite
import khelp.utilities.interval.limit.Limit
import khelp.utilities.interval.limit.LimitValue
import khelp.utilities.text.INFINITE_CHARACTER

/**
 * Represents a simple interval with minimum and maximum limits.
 *
 * @property minimum the minimum limit of the interval
 * @property maximum the maximum limit of the interval
 */
class IntervalSimple<C : Comparable<C>> internal constructor(val minimum : Limit<C>, val maximum : Limit<C>)
    : Comparable<IntervalSimple<C>>,
      Interval<C>
{
    override fun compareTo(other : IntervalSimple<C>) : Int
    {
        var comparison =
            when (this.minimum)
            {
                is Infinite<C>   ->
                    if (other.minimum is Infinite<C>) 0 else -1

                is LimitValue<C> ->
                    if (other.minimum is LimitValue<C>) this.minimum.value.compareTo(other.minimum.value) else 1
            }

        if (comparison != 0)
        {
            return comparison
        }

        if (this.minimum.include && other.minimum.include.not())
        {
            return -1
        }

        if (this.minimum.include.not() && other.minimum.include)
        {
            return 1
        }

        return when (this.maximum)
        {
            is Infinite<C>   ->
                if (other.maximum is Infinite<C>) 0 else 1

            is LimitValue<C> ->
            {
                comparison =
                    if (other.maximum is LimitValue<C>) this.maximum.value.compareTo(other.maximum.value) else -1

                when
                {
                    comparison != 0                                     -> comparison
                    this.maximum.include && other.maximum.include.not() -> 1
                    this.maximum.include.not() && other.maximum.include -> -1
                    else                                                -> 0
                }
            }
        }
    }

    override fun contains(value : C) : Boolean
    {
        if (this.minimum is LimitValue<C>)
        {
            val minimum = this.minimum.value

            if ((this.minimum.include && value < minimum) || (this.minimum.include.not() && value <= minimum))
            {
                return false
            }
        }

        if (this.maximum is LimitValue<C>)
        {
            val maximum = this.maximum.value

            if ((this.maximum.include && value > maximum) || (this.maximum.include.not() && value >= maximum))
            {
                return false
            }
        }

        return true
    }

    override fun toString() : String
    {
        var sameTest = true
        val minimumString =
            if (this.minimum is LimitValue<C>) this.minimum.value.toString()
            else
            {
                sameTest = false
                "$INFINITE_CHARACTER"
            }

        val maximumString =
            if (this.maximum is LimitValue<C>) this.maximum.value.toString()
            else
            {
                sameTest = false
                "$INFINITE_CHARACTER"
            }

        if ( sameTest && (this.minimum as LimitValue<C>).value == (this.maximum as LimitValue<C>).value)
        {
            return "{${this.minimum.value}}"
        }

        return "${if (this.minimum.include) "[" else "]"}${minimumString}, ${maximumString}${if (this.maximum.include) "]" else "["}"
    }

    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other                               -> true
            null == other || other !is IntervalSimple<*> -> false
            else                                         -> this.compareTo(other as IntervalSimple<C>) == 0
        }

    override fun hashCode() : Int =
        Objects.hash(this.minimum, this.maximum)
}