package khelp.math.extensions

import khelp.math.formal.Function

/**
 * Parse this String to a function
 */
fun String.toFunction() = Function.parse(this)
