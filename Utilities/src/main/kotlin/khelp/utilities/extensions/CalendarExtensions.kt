package khelp.utilities.extensions

import java.util.Calendar

fun Calendar.fullString(): String
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

fun calendar(year: Int, month: Int, day: Int): Calendar =
    calendar(year, month, day, 0, 0, 0, 0)

fun calendar(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Calendar =
    calendar(year, month, day, hour, minute, second, 0)

fun calendar(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, milliseconds: Int): Calendar
{
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, day, hour, minute, second)
    calendar[Calendar.MILLISECOND] = milliseconds
    return calendar
}
