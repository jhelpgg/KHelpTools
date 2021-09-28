package khelp.ui.game.interpolation

import khelp.utilities.math.Percent

object GameImageInterpolationHorizontal : GameImageInterpolationType
{
    override fun interpolate(pixelsStartImage : IntArray, pixelsEndImage : IntArray, pixelsResultImage : IntArray,
                             percent : Percent,
                             width : Int, height : Int)
    {
        val indexX = (percent.percent * width.toDouble()).toInt()

        when
        {
            indexX <= 0        -> System.arraycopy(pixelsStartImage, 0, pixelsResultImage, 0, width*height)
            indexX >= width - 1 -> System.arraycopy(pixelsEndImage, 0, pixelsResultImage, 0, width*height)
            else              ->
            {
                var index0 = 0
                var index1 = indexX
                val width1 = width -index1

                for(y in 0 until height)
                {
                    System.arraycopy(pixelsEndImage, index0, pixelsResultImage, index0, index1)
                    System.arraycopy(pixelsStartImage, index1, pixelsResultImage, index1, width1)
                    index0 += width
                    index1 += width
                }
            }
        }

    }
}
