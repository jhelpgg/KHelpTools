package khelp.utilities.extensions

import khelp.utilities.math.Percent
import khelp.utilities.math.isNul
import khelp.utilities.math.sign
import kotlin.math.floor
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

/**
 * Modulate a real inside an interval
 *
 * @param minimum  Minimum of interval
 * @param maximum  Maximum of interval
 * @return Modulated value
 */
@Throws(IllegalArgumentException::class)
fun Double.modulo(minimum : Double, maximum : Double) : Double
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

val Double.percent : Percent get() = Percent(this)
