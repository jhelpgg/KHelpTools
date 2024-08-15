package khelp.utilities.interval.limit

import khelp.utilities.text.INFINITE_CHARACTER

/**
 * Represents a limit that is based on a comparable value. It always excluded.
 *
 * @param C the type of the comparable value
 */
class Infinite<C : Comparable<C>> : Limit<C>
{
    override val include : Boolean = false

    override fun toString() : String =
        "$INFINITE_CHARACTER"

    override fun equals(other : Any?) : Boolean =
        other is Infinite<*>

    override fun hashCode() : Int =
        Infinite::class.java.hashCode()
}