package khelp.ui.game.interpolation

import khelp.resources.Resources
import khelp.ui.game.GameImageCache
import khelp.utilities.math.Percent

class GameImageInterpolationGrayTransition(private val imageGrayTransitionPath : String,
                                           private val resources : Resources) : GameImageInterpolationType
{
    override fun interpolate(pixelsStartImage : IntArray, pixelsEndImage : IntArray, pixelsResultImage : IntArray,
                             percent : Percent, width : Int, height : Int)
    {
        val percentGray = (percent.percent * 255.0).toInt()

        if (percentGray == 0)
        {
            System.arraycopy(pixelsStartImage, 0, pixelsResultImage, 0, width * height)
            return
        }

        if (percentGray == 255)
        {
            System.arraycopy(pixelsEndImage, 0, pixelsResultImage, 0, width * height)
            return
        }

        val imageGray = GameImageCache.image(this.imageGrayTransitionPath, width, height, this.resources)
        val grayPixels = imageGray.grabPixels()

        for (pixel in 0 until width * height)
        {
            pixelsResultImage[pixel] =
                if (grayPixels[pixel] and 0xFF > percentGray)
                {
                    pixelsStartImage[pixel]
                }
                else
                {
                    pixelsEndImage[pixel]
                }
        }
    }
}