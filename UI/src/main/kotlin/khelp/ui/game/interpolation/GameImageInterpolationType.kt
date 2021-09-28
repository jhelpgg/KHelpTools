package khelp.ui.game.interpolation

import khelp.utilities.math.Percent

interface GameImageInterpolationType
{
    fun interpolate(pixelsStartImage:IntArray, pixelsEndImage:IntArray, pixelsResultImage:IntArray,
                    percent:Percent,
                    width : Int, height : Int)
}

