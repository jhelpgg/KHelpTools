package khelp.utilities.extensions

import kotlin.math.max
import kotlin.math.min

fun Int.bounds(bound1: Int, bound2: Int) =
    max(min(bound1, bound2), min(max(bound1, bound2), this))
