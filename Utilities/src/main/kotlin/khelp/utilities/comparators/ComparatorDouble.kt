package khelp.utilities.comparators

import khelp.utilities.math.EPSILON
import kotlin.math.max

class ComparatorDouble(precision : Double = EPSILON) : Comparator<Double>
{
    private val precision = max(EPSILON, precision)

    override fun compare(first : Double, second : Double) : Int
    {
        val difference = first - second

        return when
        {
            difference > this.precision  -> 1
            difference < -this.precision -> -1
            else                         -> 0
        }
    }
}