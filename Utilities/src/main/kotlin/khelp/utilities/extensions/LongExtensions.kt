package khelp.utilities.extensions

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Limit an integer inside given bounds
 *
 * * If the integer is less than the minimum of given bounds, the minimum of bounds is returned
 * * If the integer is more than the maximum of given bounds, the maximum of bounds is returned
 * * Else, it means the integer is inside bounds, so the integer itself is returned
 */
fun Long.bounds(bound1: Long, bound2: Long) =
    max(min(bound1, bound2), min(max(bound1, bound2), this))


/**
 * Compute the greater common divider of this number and given one
 * @param long Given number
 * @return The greater common divider
 */
infix fun Long.GCD(long: Long): Long
{
    val absoluteLong1 = abs(this)
    val absoluteLong2 = abs(long)
    var minimum = min(absoluteLong1, absoluteLong2)
    var maximum = max(absoluteLong1, absoluteLong2)
    var temporary: Long

    while (minimum > 0)
    {
        temporary = minimum
        minimum = maximum % minimum
        maximum = temporary
    }

    return maximum
}


/**
 * Compute the lower common multiple of this number and given one
 * @param long Given number
 * @return The lower common multiple
 */
infix fun Long.LCM(long: Long): Long
{
    val gcd = this GCD long

    if (gcd == 0L)
    {
        return 0L
    }

    return this * (long / gcd)
}