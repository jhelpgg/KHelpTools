package khelp.algorithm.interpolation

import khelp.image.Color
import khelp.utilities.collections.SortedArray
import khelp.utilities.math.compare

class ColorScale(private val minimum : Float, colorMinimum : Color, private val maximum : Float, colorMaximum : Color)
{
    private val colors = SortedArray<ColorScaleElement>(true)

    init
    {
        if (this.maximum < this.minimum)
        {
            throw IllegalArgumentException("minimum (${this.minimum}) is not <= at maximum (${this.maximum})")
        }

        this.colors.add(ColorScaleElement(this.minimum, colorMinimum))
        this.colors.add(ColorScaleElement(this.maximum, colorMaximum))
    }

    fun add(value : Float, color : Color)
    {
        if (compare(value, this.minimum) <= 0 || compare(value, this.maximum) >= 0)
        {
            return
        }

        val colorScaleElement = ColorScaleElement(value, color)
        this.colors.remove(colorScaleElement)
        this.colors += colorScaleElement
    }

    fun color(value : Float) : Int
    {
        if (compare(value, this.minimum) <= 0) return this.colors[0].color.color

        if (compare(value, this.maximum) >= 0) return this.colors[this.colors.size - 1].color.color

        var index = 1

        while (index < this.colors.size - 1 && compare(value, this.colors[index].value) > 0)
        {
            index++
        }

        val (valueBefore, colorBefore) = this.colors[index - 1]
        val (valueAfter, colorAfter) = this.colors[index]
        val factor = (value - valueBefore) / (valueAfter - valueBefore)
        val rotcaf = 1f - factor
        val alpha = (colorBefore.alpha * rotcaf + colorAfter.alpha * factor).toInt()
        val red = (colorBefore.red * rotcaf + colorAfter.red * factor).toInt()
        val green = (colorBefore.green * rotcaf + colorAfter.green * factor).toInt()
        val blue = (colorBefore.blue * rotcaf + colorAfter.blue * factor).toInt()
        return (alpha shl 24) or (red shl 16) or (green shl 8) or blue
    }
}