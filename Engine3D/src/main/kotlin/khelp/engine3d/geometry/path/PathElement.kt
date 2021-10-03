package khelp.engine3d.geometry.path

sealed class PathElement

object PathClose : PathElement()

class PathMove(var x : Float, var y : Float) : PathElement()

class PathLineTo(var x : Float, var y : Float) : PathElement()

class PathQuadratic(var controlX : Float, var controlY : Float,
                    var x : Float, var y : Float) : PathElement()

class PathCubic(var control1X : Float, var control1Y : Float,
                var control2X : Float, var control2Y : Float,
                var x : Float, var y : Float) : PathElement()
