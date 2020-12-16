package khelp.utilities.extensions

import kotlin.math.max
import kotlin.math.min

/**
 * Limit an integer inside given bounds
 *
 * * If the integer is less than the minimum of given bounds, the minimum of bounds is returned
 * * If the integer is more than the maximum of given bounds, the maximum of bounds is returned
 * * Else, it means the integer is inside bounds, so the integer itself is returned
 */
fun Int.bounds(bound1: Int, bound2: Int) =
    max(min(bound1, bound2), min(max(bound1, bound2), this))
