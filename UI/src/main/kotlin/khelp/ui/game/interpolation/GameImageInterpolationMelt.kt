package khelp.ui.game.interpolation

import khelp.utilities.extensions.alpha
import khelp.utilities.extensions.blue
import khelp.utilities.extensions.green
import khelp.utilities.extensions.red
import khelp.utilities.math.Percent

object GameImageInterpolationMelt : GameImageInterpolationType
{
    override fun interpolate(pixelsStartImage : IntArray, pixelsEndImage : IntArray, pixelsResultImage : IntArray,
                             percent : Percent,
                             width : Int, height : Int)
    {
        val size = width * height
        val endPercent = percent.percent
        val startPercent = 1.0 - endPercent

        for (pixel in 0 until size)
        {
            pixelsResultImage[pixel] = meltColor(pixelsStartImage[pixel], startPercent,
                                                 pixelsEndImage[pixel], endPercent)
        }
    }

    private fun meltColor(colorStart : Int, startPercent : Double, colorEnd : Int, endPercent : Double) : Int =
        (meltPart(colorStart.alpha, startPercent, colorEnd.alpha, endPercent) shl 24) or
                (meltPart(colorStart.red, startPercent, colorEnd.red, endPercent) shl 16) or
                (meltPart(colorStart.green, startPercent, colorEnd.green, endPercent) shl 8) or
                meltPart(colorStart.blue, startPercent, colorEnd.blue, endPercent)

    private fun meltPart(partStart : Int, startPercent : Double, partEnd : Int, endPercent : Double) : Int =
        (partStart * startPercent + partEnd * endPercent).toInt()
}