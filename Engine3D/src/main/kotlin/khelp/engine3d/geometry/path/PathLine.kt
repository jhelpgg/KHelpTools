package khelp.engine3d.geometry.path

import khelp.utilities.math.square
import kotlin.math.sqrt

class PathLine(val x1 : Float, val y1 : Float, var information1 : Float,
               val x2 : Float, val y2 : Float, var information2 : Float)
{
    val distance : Float = sqrt(square(this.x1 - this.x2) + square(this.y1 - this.y2))
}
