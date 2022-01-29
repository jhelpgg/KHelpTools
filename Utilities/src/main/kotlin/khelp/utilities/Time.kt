package khelp.utilities

import khelp.utilities.extensions.bounds
import khelp.utilities.text.conjugatePlural
import java.util.Objects

class Time(milliseconds : Long = 0L) : Comparable<Time>
{
    constructor(milliseconds : Int = 0) : this(milliseconds.toLong())

    val milliseconds = milliseconds.bounds(0, Long.MAX_VALUE)
    val seconds = this.milliseconds / 1000L
    val minutes = this.seconds / 60L
    val hours = this.minutes / 60L
    val days = this.hours / 24L
    val partMilliseconds = (this.milliseconds % 1000L).toInt()
    val partSeconds = (this.seconds % 60L).toInt()
    val partMinutes = (this.minutes % 60L).toInt()
    val partHours = (this.hours % 24L).toInt()

    operator fun plus(time : Time) : Time = Time(this.milliseconds + time.milliseconds)
    operator fun plus(milliseconds : Long) : Time = Time(this.milliseconds + milliseconds)
    operator fun plus(milliseconds : Int) : Time = Time(this.milliseconds + milliseconds)
    operator fun minus(time : Time) : Time = Time(this.milliseconds - time.milliseconds)
    operator fun minus(milliseconds : Long) : Time = Time(this.milliseconds - milliseconds)
    operator fun minus(milliseconds : Int) : Time = Time(this.milliseconds - milliseconds)

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

    override fun hashCode() : Int = Objects.hash(this.milliseconds)

    override fun compareTo(other : Time) : Int
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