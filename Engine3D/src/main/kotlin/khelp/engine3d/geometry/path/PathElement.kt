package khelp.engine3d.geometry.path

sealed class PathElement

object PathClose : PathElement()

class PathMove(val x : Float, val y : Float) : PathElement()

class PathLineTo(val x : Float, val y : Float) : PathElement()

class PathQuadratic(val controlX : Float, val controlY : Float,
                    val x : Float, val y : Float) : PathElement()

class PathCubic(val control1X : Float, val control1Y : Float,
                val control2X : Float, val control2Y : Float,
                val x : Float, val y : Float) : PathElement()
