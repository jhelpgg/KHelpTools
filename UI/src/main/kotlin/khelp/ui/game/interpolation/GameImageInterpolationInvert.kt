package khelp.ui.game.interpolation

import khelp.utilities.math.Percent

class GameImageInterpolationInvert(private val gameImageInterpolationType : GameImageInterpolationType)
    : GameImageInterpolationType
{
    override fun interpolate(pixelsStartImage : IntArray, pixelsEndImage : IntArray, pixelsResultImage : IntArray,
                             percent : Percent,
                             width : Int, height : Int) =
        this.gameImageInterpolationType.interpolate(pixelsStartImage, pixelsEndImage, pixelsResultImage,
                                                    Percent(1.0 - percent.percent),
                                                    width, height)
}