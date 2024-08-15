package khelp.utilities.comparators

import khelp.utilities.math.EPSILON_FLOAT
import kotlin.math.max

class ComparatorFloat(precision : Float = EPSILON_FLOAT) : Comparator<Float>
{
    private val precision = max(EPSILON_FLOAT, precision)

    override fun compare(first : Float, second : Float) : Int
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