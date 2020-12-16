package khelp.database.type

import java.util.Calendar

private const val DAY_MASK = 0b0000_0000_0000_0000_0000_0000_0001_1111
private const val MONTH_MASK = 0b0000_0000_0000_0000_0000_0001_1110_0000
private const val YEAR_MASK = 0b0000_0001_1111_1111_1111_1110_0000_0000
private const val MONTH_SHIFT = 5
private const val YEAR_SHIFT = MONTH_SHIFT + 4

/**
 * A day date : year/month/day
 */
class DataDate(year: Int, month: Int, day: Int) : Comparable<DataDate>
{
    val year: Int
    val month: Int
    val day: Int
    internal val serialized: Int

    init
    {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        this.year = calendar.get(Calendar.YEAR)
        this.month = calendar.get(Calendar.MONTH) + 1
        this.day = calendar.get(Calendar.DAY_OF_MONTH)
        this.serialized = (this.year shl YEAR_SHIFT) or
                (this.month shl MONTH_SHIFT) or
                this.day
    }

    constructor(calendar: Calendar = Calendar.getInstance()) :
            this(calendar.get(Calendar.YEAR),
                 calendar.get(Calendar.MONTH) + 1,
                 calendar.get(Calendar.DAY_OF_MONTH))

    internal constructor(serialized: Int) :
            this((serialized and YEAR_MASK) shr YEAR_SHIFT,
                 (serialized and MONTH_MASK) shr MONTH_SHIFT,
                 serialized and DAY_MASK)

    override operator fun compareTo(other: DataDate): Int =
        this.serialized - other.serialized

    override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is DataDate)
        {
            return false
        }

        return this.serialized == other.serialized
    }

    override fun hashCode(): Int =
        this.serialized

    override fun toString(): String =
        String.format("%04d/%02d/%02d", this.year, this.month, this.day)
}
