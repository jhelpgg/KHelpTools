package khelp.utilities.extensions

import khelp.utilities.math.isNul
import khelp.utilities.math.sign
import kotlin.math.max
import kotlin.math.min

/**
 * Limit a real inside given bounds
 *
 * * If the real is less than the minimum of given bounds, the minimum of bounds is returned
 * * If the real is more than the maximum of given bounds, the maximum of bounds is returned
 * * Else, it means the integer is inside bounds, so the real itself is returned
 */
fun Double.bounds(bound1 : Double, bound2 : Double) : Double =
    max(min(bound1, bound2), min(max(bound1, bound2), this))

fun Double.compare(number : Double) : Int =
    sign(this - number)

val Double.nul : Boolean get() = isNul(this)
