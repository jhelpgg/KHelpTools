package khelp.utilities.extensions

import khelp.utilities.math.isNul
import khelp.utilities.math.sign
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

/**
 * Limit an integer inside given bounds
 *
 * * If the integer is less than the minimum of given bounds, the minimum of bounds is returned
 * * If the integer is more than the maximum of given bounds, the maximum of bounds is returned
 * * Else, it means the integer is inside bounds, so the integer itself is returned
 */
fun Float.bounds(bound1 : Float, bound2 : Float) =
    max(min(bound1, bound2), min(max(bound1, bound2), this))

fun Float.compare(number : Float) : Int =
    sign(this - number)

val Float.nul : Boolean get() = isNul(this)

/**
 * Modulate a real inside an interval
 *
 * @param minimum  Minimum of interval
 * @param maximum  Maximum of interval
 * @return Modulated value
 */
@Throws(IllegalArgumentException::class)
fun Float.modulo(minimum : Float, maximum : Float) : Float
{
    var real = this
    val min = min(minimum, maximum)
    val max = max(min, maximum)

    if (real in min .. max)
    {
        return real
    }

    val space = max - min

    if (isNul(space))
    {
        throw IllegalArgumentException("Can't take modulo in empty interval")
    }

    real = (real - min) / space

    return (space * (real - floor(real))) + min
}
