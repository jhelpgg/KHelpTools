package khelp.utilities.extensions

import java.util.Calendar

fun Calendar.fullString() : String
{
    val stringBuilder = StringBuilder()
    stringBuilder.appendMinimumDigit(4, this[Calendar.YEAR])
    stringBuilder.append('/')
    stringBuilder.appendMinimumDigit(2, this[Calendar.MONTH] + 1)
    stringBuilder.append('/')
    stringBuilder.appendMinimumDigit(2, this[Calendar.DAY_OF_MONTH])
    stringBuilder.append(':')
    stringBuilder.appendMinimumDigit(2, this[Calendar.HOUR_OF_DAY])
    stringBuilder.append('H')
    stringBuilder.appendMinimumDigit(2, this[Calendar.MINUTE])
    stringBuilder.append('M')
    stringBuilder.appendMinimumDigit(2, this[Calendar.SECOND])
    stringBuilder.append('S')
    stringBuilder.appendMinimumDigit(3, this[Calendar.MILLISECOND])
    return stringBuilder.toString()
}

fun calendar(year : Int, month : Int, day : Int) : Calendar =
    calendar(year, month, day, 0, 0, 0, 0)

fun calendar(year : Int, month : Int, day : Int, hour : Int, minute : Int, second : Int) : Calendar =
    calendar(year, month, day, hour, minute, second, 0)

fun calendar(year : Int, month : Int, day : Int, hour : Int, minute : Int, second : Int, milliseconds : Int) : Calendar
{
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, day, hour, minute, second)
    calendar[Calendar.MILLISECOND] = milliseconds
    return calendar
}

var Calendar.year : Int
    get() = this[Calendar.YEAR]
    set(value)
    {
        this[Calendar.YEAR] = value
    }

var Calendar.month : Int
    get() = this[Calendar.MONTH] + 1
    set(value)
    {
        this[Calendar.MONTH] = value - 1
    }

var Calendar.day : Int
    get() = this[Calendar.DAY_OF_MONTH]
    set(value)
    {
        this[Calendar.DAY_OF_MONTH] = value
    }

var Calendar.hour : Int
    get() = this[Calendar.HOUR_OF_DAY]
    set(value)
    {
        this[Calendar.HOUR_OF_DAY] = value
    }

var Calendar.minute : Int
    get() = this[Calendar.MINUTE]
    set(value)
    {
        this[Calendar.MINUTE] = value
    }

var Calendar.second : Int
    get() = this[Calendar.SECOND]
    set(value)
    {
        this[Calendar.SECOND] = value
    }

var Calendar.millisecond : Int
    get() = this[Calendar.MILLISECOND]
    set(value)
    {
        this[Calendar.MILLISECOND] = value
    }

val Calendar.age : Int
    get()
    {
        val today = Calendar.getInstance()
        val age = today.year - this.year
        val monthDiff = today.month - this.month

        if (monthDiff < 0 || (monthDiff == 0 && today.day < this.day))
        {
            return age - 1
        }

        return age
    }
