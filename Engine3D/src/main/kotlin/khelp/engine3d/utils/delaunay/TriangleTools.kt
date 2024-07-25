package khelp.engine3d.utils.delaunay

import khelp.engine3d.extensions.PI_FLOAT
import kotlin.math.cos
import kotlin.math.sin

val cos2PI3 = cos(2f * PI_FLOAT / 3f)
val sin2PI3 = sin(2f * PI_FLOAT / 3f)
val cos4PI3 = cos(4f * PI_FLOAT / 3f)
val sin4PI3 = sin(4f * PI_FLOAT / 3f)

fun triangleOfInscribedCircle(enclosingCircle : EnclosingCircle, factor : Float = 1f) : TriangleIndexed
{
    val ray = enclosingCircle.ray * factor * 2f
    val centerX = enclosingCircle.centerX
    val centerY = enclosingCircle.centerY
    val point1 = PointIndexed(-1, centerX + ray, centerY)
    val point2 = PointIndexed(-2, centerX + ray * cos2PI3, centerY + ray * sin2PI3)
    val point3 = PointIndexed(-3, centerX + ray * cos4PI3, centerY + ray * sin4PI3)
    return TriangleIndexed(point1, point2, point3)
}