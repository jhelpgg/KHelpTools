package khelp.utilities.interval.limit

import java.util.Objects

/**
 * Represents a limit value with optional inclusion/exclusion.
 *
 * @property value the value of the limit
 * @property include specifies whether the value is included or excluded
 */
class LimitValue<C:Comparable<C>> (val value: C, override val include : Boolean ) : Limit<C>
{
    override fun toString() : String =
        "${if(this.include) "include" else "exclude"} : ${this.value}"

    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other -> true
            null == other || other !is LimitValue<*> -> false
            else -> this.value == other.value
        }

    override fun hashCode() : Int =
        Objects.hash(this.value, this.include)
}