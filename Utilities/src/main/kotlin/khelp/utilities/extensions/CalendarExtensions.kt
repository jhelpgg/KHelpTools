package khelp.utilities.extensions

import java.util.Calendar

fun Calendar.fullString() : String
{
    val stringBuilder = StringBuilder()
    stringBuilder.appendMinimumSize(4, this[Calendar.YEAR])
    stringBuilder.append('/')
    stringBuilder.appendMinimumSize(2, this[Calendar.MONTH]+1)
    stringBuilder.append('/')
    stringBuilder.appendMinimumSize(2, this[Calendar.DAY_OF_MONTH])
    stringBuilder.append(':')
    stringBuilder.appendMinimumSize(2, this[Calendar.HOUR_OF_DAY])
    stringBuilder.append('H')
    stringBuilder.appendMinimumSize(2, this[Calendar.MINUTE])
    stringBuilder.append('M')
    stringBuilder.appendMinimumSize(2, this[Calendar.SECOND])
    stringBuilder.append('S')
    stringBuilder.appendMinimumSize(3, this[Calendar.MILLISECOND])
    return stringBuilder.toString()
}