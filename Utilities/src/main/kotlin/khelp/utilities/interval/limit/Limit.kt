package khelp.utilities.interval.limit

/**
 * Represents a limit based on comparable. Is can be included or excluded
 */
sealed interface Limit<C:Comparable<C>>
{
    /**
     * Boolean value representing whether the limit is included or excluded.
     */
    val include : Boolean

    override fun toString() : String

    override fun equals(other: Any?) : Boolean

    override fun hashCode() : Int
}