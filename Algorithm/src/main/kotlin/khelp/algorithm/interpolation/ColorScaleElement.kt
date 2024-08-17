package khelp.algorithm.interpolation

import khelp.image.Color
import khelp.utilities.math.compare

data class ColorScaleElement(val value : Float, val color : Color) : Comparable<ColorScaleElement>
{
    override fun compareTo(other : ColorScaleElement) : Int =
        compare(this.value, other.value)

    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other                               -> true
            null == other || other !is ColorScaleElement -> false
            else                                         -> khelp.utilities.math.equals(this.value, other.value)
        }

    override fun hashCode() : Int =
        this.value.hashCode()
}
