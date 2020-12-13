package khelp.database.type

import java.util.Calendar
import khelp.utilities.extensions.bounds

private const val MILLISECONDS_MASK = 0b0000_0000_0000_0000_0000_0011_1111_1111
private const val SECONDS_MASK = 0b0000_0000_0000_0000_1111_1100_0000_0000
private const val MINUTES_MASK = 0b0000_0000_0011_1111_0000_0000_0000_0000
private const val HOURS_MASK = 0b0000_0111_1100_0000_0000_0000_0000_0000
private const val DAYS_MASK = 0b1111_1000_0000_0000_0000_0000_0000_0000.toInt()
private const val SECONDS_SHIFT = 10
private const val MINUTES_SHIFT = SECONDS_SHIFT + 6
private const val HOURS_SHIFT = MINUTES_SHIFT + 6
private const val DAYS_SHIFT = HOURS_SHIFT + 5
private const val MAX_DAYS = 0b1_1111

class DataTime() : Comparable<DataTime>
{
    var days = 0
        private set
    var hours = 0
        private set
    var minutes = 0
        private set
    var seconds = 0
        private set
    var milliseconds = 0
        private set
    internal var serialized = 0
        private set

    init
    {
        this.now()
    }

    constructor(dataTime: DataTime) : this()
    {
        this.copy(dataTime)
    }

    constructor(hours: Int, minutes: Int, seconds: Int, milliseconds: Int) : this()
    {
        this.days = 0
        this.hours = hours
        this.minutes = minutes
        this.seconds = seconds
        this.milliseconds = milliseconds
        this.validate()
    }

    internal constructor(serialized: Int) : this()
    {
        this.parse(serialized)
    }

    fun copy(dataTime: DataTime)
    {
        this.days = dataTime.days
        this.hours = dataTime.hours
        this.minutes = dataTime.minutes
        this.seconds = dataTime.seconds
        this.milliseconds = dataTime.milliseconds
        this.serialized = dataTime.serialized
    }

    fun now()
    {
        this.days = 0
        val calendar = Calendar.getInstance()
        this.hours = calendar.get(Calendar.HOUR_OF_DAY)
        this.minutes = calendar.get(Calendar.MINUTE)
        this.seconds = calendar.get(Calendar.SECOND)
        this.milliseconds = calendar.get(Calendar.MILLISECOND)
        this.update()
    }

    fun addMilliseconds(milliseconds: Int)
    {
        this.milliseconds += milliseconds
        this.validate()
    }

    fun subtractMilliseconds(milliseconds: Int)
    {
        this.milliseconds -= milliseconds
        this.validate()
    }

    fun addSeconds(seconds: Int)
    {
        this.seconds += seconds
        this.validate()
    }

    fun subtractSeconds(seconds: Int)
    {
        this.seconds -= seconds
        this.validate()
    }

    fun addMinutes(minutes: Int)
    {
        this.minutes += minutes
        this.validate()
    }

    fun subtractMinutes(minutes: Int)
    {
        this.minutes -= minutes
        this.validate()
    }

    fun addHours(hours: Int)
    {
        this.hours += hours
        this.validate()
    }

    fun subtractHours(hours: Int)
    {
        this.hours -= hours
        this.validate()
    }


    fun add(hours: Int = 0, minutes: Int = 0, seconds: Int = 0, milliseconds: Int = 0)
    {
        this.hours += hours
        this.minutes += minutes
        this.seconds += seconds
        this.milliseconds += milliseconds
        this.validate()
    }

    fun subtract(hours: Int = 0, minutes: Int = 0, seconds: Int = 0, milliseconds: Int = 0)
    {
        this.hours -= hours
        this.minutes -= minutes
        this.seconds -= seconds
        this.milliseconds -= milliseconds
        this.validate()
    }

    operator fun plusAssign(time: DataTime)
    {
        this.add(time.hours, time.minutes, time.seconds, time.milliseconds)
    }

    operator fun plus(time: DataTime): DataTime
    {
        val dataTime = DataTime(this)
        dataTime += time
        return dataTime
    }

    operator fun minusAssign(time: DataTime)
    {
        this.subtract(time.hours, time.minutes, time.seconds, time.milliseconds)
    }

    operator fun minus(time: DataTime): DataTime
    {
        val dataTime = DataTime(this)
        dataTime -= time
        return dataTime
    }

    override operator fun compareTo(other: DataTime): Int
    {
        var comparison = this.days - other.days

        if (comparison != 0)
        {
            return comparison
        }

        comparison = this.hours - other.hours

        if (comparison != 0)
        {
            return comparison
        }

        comparison = this.minutes - other.minutes

        if (comparison != 0)
        {
            return comparison
        }

        comparison = this.seconds - other.seconds

        if (comparison != 0)
        {
            return comparison
        }

        return this.milliseconds - other.milliseconds
    }

    override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is DataTime)
        {
            return false
        }

        return this.serialized == other.serialized
    }

    override fun hashCode(): Int =
        this.serialized

    override fun toString(): String
    {
        val stringBuilder = StringBuilder()
        var writing = false

        if (this.days > 0)
        {
            writing = true
            stringBuilder.append(this.days)
            stringBuilder.append("d ")
        }

        if (writing || this.hours > 0)
        {
            writing = true
            stringBuilder.append(String.format("%02d", this.hours))
            stringBuilder.append("H ")
        }

        if (writing || this.minutes > 0)
        {
            writing = true
            stringBuilder.append(String.format("%02d", this.minutes))
            stringBuilder.append("M ")
        }

        if (writing || this.seconds > 0)
        {
            stringBuilder.append(String.format("%02d", this.seconds))
            stringBuilder.append("S ")
        }

        stringBuilder.append(String.format("%03d", this.milliseconds))
        stringBuilder.append("MS")

        return stringBuilder.toString()
    }

    private fun parse(serialized: Int)
    {
        this.serialized = serialized
        this.days = (serialized and DAYS_MASK) shr DAYS_SHIFT
        this.hours = (serialized and HOURS_MASK) shr HOURS_SHIFT
        this.minutes = (serialized and MINUTES_MASK) shr MINUTES_SHIFT
        this.seconds = (serialized and SECONDS_MASK) shr SECONDS_SHIFT
        this.milliseconds = serialized and MILLISECONDS_MASK
    }

    private fun validate()
    {
        while (this.milliseconds < 0)
        {
            this.seconds--
            this.milliseconds += 1000
        }

        this.seconds += this.milliseconds / 1000
        this.milliseconds %= 1000

        while (this.seconds < 0)
        {
            this.minutes--
            this.seconds += 60
        }

        this.minutes += this.seconds / 60
        this.seconds %= 60

        while (this.minutes < 0)
        {
            this.hours--
            this.minutes += 60
        }

        this.hours += this.minutes / 60
        this.minutes %= 60

        while (this.hours < 0)
        {
            this.days--
            this.hours += 24
        }

        this.days += this.hours / 24
        this.hours %= 24

        this.days = this.days.bounds(0, MAX_DAYS)

        this.update()
    }

    private fun update()
    {
        this.serialized = (this.days shl DAYS_SHIFT) or
                (this.hours shl HOURS_SHIFT) or
                (this.milliseconds shl MINUTES_SHIFT) or
                (this.seconds shl SECONDS_SHIFT) or
                this.milliseconds
    }
}
