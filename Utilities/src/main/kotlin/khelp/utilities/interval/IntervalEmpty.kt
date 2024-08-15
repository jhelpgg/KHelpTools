package khelp.utilities.interval

/**
 * Represents an empty interval of possible comparable values.
 * This interval contains no values.
 */
class IntervalEmpty<C:Comparable<C>> : Interval<C>
{
    override fun contains(value : C) : Boolean =
        false

    override fun toString() : String =
        "{}"

    override fun equals(other : Any?) : Boolean =
        other is IntervalEmpty<*>

    override fun hashCode() : Int =
        IntervalEmpty::class.java.hashCode()
}