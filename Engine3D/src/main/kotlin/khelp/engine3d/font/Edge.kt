package khelp.engine3d.font

import khelp.utilities.math.square
import kotlin.math.sqrt

internal class Edge(val x1 : Float, val y1 : Float, val x2 : Float, val y2 : Float)
{
    var before:Float = 0f
    var weight : Float = sqrt(square(this.x2 - this.x1) + square(this.y2 - this.y1))
}
