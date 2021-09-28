package khelp.ui.game.interpolation

import khelp.utilities.math.Percent

object GameImageInterpolationVertical : GameImageInterpolationType
{
    override fun interpolate(pixelsStartImage : IntArray, pixelsEndImage : IntArray, pixelsResultImage : IntArray,
                             percent : Percent,
                             width : Int, height : Int)
    {
        val indexY = (percent.percent * height.toDouble()).toInt()

        when
        {
            indexY <= 0          -> System.arraycopy(pixelsStartImage, 0, pixelsResultImage, 0, width * height)
            indexY >= height - 1 -> System.arraycopy(pixelsEndImage, 0, pixelsResultImage, 0, width * height)
            else                 ->
            {
                val index = width * indexY
                System.arraycopy(pixelsEndImage, 0, pixelsResultImage, 0, index)
                System.arraycopy(pixelsStartImage, index, pixelsResultImage, index, width * height - index)
            }
        }
    }
}