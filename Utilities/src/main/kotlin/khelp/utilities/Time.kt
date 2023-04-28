package khelp.utilities

import khelp.utilities.extensions.bounds
import khelp.utilities.text.conjugatePlural
import java.util.Objects

/**
 * Represents a duration
 * @param milliseconds Duration in milliseconds
 */
class Time(milliseconds : Long = 0L) : Comparable<Time>
{
    /**
     * @param milliseconds Duration in milliseconds
     */
    constructor(milliseconds : Int = 0) : this(milliseconds.toLong())

    /** Duration in milliseconds */
    val milliseconds = milliseconds.bounds(0, Long.MAX_VALUE)

    /** Duration in seconds */
    val seconds = this.milliseconds / 1000L

    /** Duration in minutes */
    val minutes = this.seconds / 60L

    /** Duration in hours */
    val hours = this.minutes / 60L

    /** Duration in days */
    val days = this.hours / 24L

    /** Part of milliseconds of the duration */
    val partMilliseconds = (this.milliseconds % 1000L).toInt()

    /** Part of seconds of the duration */
    val partSeconds = (this.seconds % 60L).toInt()

    /** Part of minutes of the duration */
    val partMinutes = (this.minutes % 60L).toInt()

    /** Part of hours of the duration */
    val partHours = (this.hours % 24L).toInt()

    /**Add another duration*/
    operator fun plus(time : Time) : Time = Time(this.milliseconds + time.milliseconds)

    /**Add a number of milliseconds*/
    operator fun plus(milliseconds : Long) : Time = Time(this.milliseconds + milliseconds)

    /**Add a number of milliseconds*/
    operator fun plus(milliseconds : Int) : Time = Time(this.milliseconds + milliseconds)

    /**Subtract another duration*/
    operator fun minus(time : Time) : Time = Time(this.milliseconds - time.milliseconds)

    /**Subtract a number of milliseconds*/
    operator fun minus(milliseconds : Long) : Time = Time(this.milliseconds - milliseconds)

    /**Subtract a number of milliseconds*/
    operator fun minus(milliseconds : Int) : Time = Time(this.milliseconds - milliseconds)

    /**
     * String representation
     */
    override fun toString() : String
    {
        var firstPart = ""

        if (this.days > 0)
        {
            firstPart = conjugatePlural("{0} day{0|0|s} ", this.days.toInt())
        }

        if (this.partHours > 0)
        {
            if (firstPart.isEmpty())
            {
                firstPart = conjugatePlural("{0} hour{0|0|s} ", this.partHours)
            }
            else
            {
                return "$firstPart${conjugatePlural("{0} hour{0|0|s}", this.partHours)}"
            }
        }

        if (this.partMinutes > 0)
        {
            if (firstPart.isEmpty())
            {
                firstPart = conjugatePlural("{0} minute{0|0|s} ", this.partMinutes)
            }
            else
            {
                return "$firstPart${conjugatePlural("{0} minute{0|0|s}", this.partMinutes)}"
            }
        }

        if (this.partSeconds > 0)
        {
            if (firstPart.isEmpty())
            {
                firstPart = conjugatePlural("{0} second{0|0|s} ", this.partSeconds)
            }
            else
            {
                return "$firstPart${conjugatePlural("{0} second{0|0|s}", this.partSeconds)}"
            }
        }

        if (this.partMilliseconds == 0)
        {
            return if (firstPart.isEmpty())
            {
                "0 millisecond"
            }
            else
            {
                firstPart.trim()
            }
        }

        return "$firstPart${conjugatePlural("{0} millisecond{0|0|s}", this.partMilliseconds)}"
    }

    /**
     * Indicates if an other object is equivalent to this duration
     */
    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other -> true
            null == other  -> false
            other is Int   -> this.milliseconds == other.toLong()
            other is Long  -> this.milliseconds == other
            other is Time  -> this.milliseconds == other.milliseconds
            else           -> false
        }

    /**
     * Hash code
     */
    override fun hashCode() : Int = Objects.hash(this.milliseconds)

    /**
     * Compare with another duration
     */
    override operator fun compareTo(other : Time) : Int
    {
        val comparison = this.milliseconds - other.milliseconds

        return when
        {
            comparison < 0L  -> - 1
            comparison == 0L -> 0
            else             -> 1
        }
    }
}