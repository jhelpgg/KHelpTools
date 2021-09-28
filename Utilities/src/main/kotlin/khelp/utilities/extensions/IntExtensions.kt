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
fun Int.bounds(bound1 : Int, bound2 : Int) =
    max(min(bound1, bound2), min(max(bound1, bound2), this))

/**
 * Compute the greater common divider of this number and given one
 * @param int Given number
 * @return The greater common divider
 */
infix fun Int.GCD(int : Int) : Int
{
    val absoluteLong1 = abs(this)
    val absoluteLong2 = abs(int)
    var minimum = min(absoluteLong1, absoluteLong2)
    var maximum = max(absoluteLong1, absoluteLong2)
    var temporary : Int

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
 * @param int Given number
 * @return The lower common multiple
 */
infix fun Int.LCM(int : Int) : Int
{
    val gcd = this GCD int

    if (gcd == 0)
    {
        return 0
    }

    return this * (int / gcd)
}

val Int.alpha : Int get() = (this shr 24) and 0xFF

val Int.red : Int get() = (this shr 16) and 0xFF

val Int.green : Int get() = (this shr 8) and 0xFF

val Int.blue : Int get() = this and 0xFF
