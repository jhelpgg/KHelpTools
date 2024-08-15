package khelp.utilities.interval

/**
 * Represents an interval of possible comparable.
 * An interval can be inclusive or exclusive on its limits.
 */
sealed interface Interval<C : Comparable<C>>
{
    /**
     * Checks if the specified value is present within the interval.
     *
     * @param value the value to check if it is within the interval.
     * @return `true` if the value is within the interval, `false` otherwise.
     */
    operator fun contains(value : C) : Boolean

    override fun toString() : String

    override fun equals(other: Any?) : Boolean

    override fun hashCode() : Int
}