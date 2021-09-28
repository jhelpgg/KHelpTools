package khelp.ui.game.interpolation

import khelp.utilities.math.Percent

object GameImageInterpolationProgress : GameImageInterpolationType
{
    override fun interpolate(pixelsStartImage : IntArray, pixelsEndImage : IntArray, pixelsResultImage : IntArray,
                             percent : Percent,
                             width : Int, height : Int)
    {
        val size = width * height
        val index = (percent.percent * size.toDouble()).toInt()

        when
        {
            index <= 0        -> System.arraycopy(pixelsStartImage, 0, pixelsResultImage, 0, size)
            index >= size - 1 -> System.arraycopy(pixelsEndImage, 0, pixelsResultImage, 0, size)
            else              ->
            {
                System.arraycopy(pixelsEndImage, 0, pixelsResultImage, 0, index)
                System.arraycopy(pixelsStartImage, index, pixelsResultImage, index, size - index)
            }
        }
    }
}