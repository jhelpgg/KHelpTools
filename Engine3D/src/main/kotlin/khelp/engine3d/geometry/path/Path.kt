package khelp.engine3d.geometry.path

import khelp.engine3d.utils.cubics
import khelp.engine3d.utils.quadratics
import khelp.utilities.math.isNul
import kotlin.math.max

class Path
{
    private val pathElements = ArrayList<PathElement>()

    fun close()
    {
        this.pathElements.add(PathClose)
    }

    fun move(x : Float, y : Float)
    {
        this.pathElements.add(PathMove(x, y))
    }

    fun line(x : Float, y : Float)
    {
        this.pathElements.add(PathLineTo(x, y))
    }

    fun quadratic(controlX : Float, controlY : Float,
                  x : Float, y : Float)
    {
        this.pathElements.add(PathQuadratic(controlX, controlY,
                                            x, y))
    }

    fun cubic(control1X : Float, control1Y : Float,
              control2X : Float, control2Y : Float,
              x : Float, y : Float)
    {
        this.pathElements.add(PathCubic(control1X, control1Y,
                                        control2X, control2Y,
                                        x, y))
    }

    fun path(precision : Int = 5, start : Float = 0f, end : Float = 1f) : List<PathLine>
    {
        @Suppress("NAME_SHADOWING")
        val precision = max(2, precision)
        val lines = ArrayList<PathLine>()
        var xStart = 0f
        var yStart = 0f
        var x = 0f
        var y = 0f
        var distance = 0f
        var line : PathLine
        var xs : FloatArray
        var ys : FloatArray

        for (element in this.pathElements)
        {
            when (element)
            {
                PathClose        ->
                {
                    if (! khelp.utilities.math.equals(x, xStart) || ! khelp.utilities.math.equals(y, yStart))
                    {
                        line = PathLine(x, y, 0f, xStart, yStart, 1f)
                        distance += line.distance
                        lines.add(line)
                    }

                    x = xStart
                    y = yStart
                }
                is PathMove      ->
                {
                    xStart = element.x
                    yStart = element.y
                    x = element.x
                    y = element.y
                }
                is PathLineTo    ->
                {
                    line = PathLine(x, y, 0f, element.x, element.y, 1f)
                    distance += line.distance
                    lines.add(line)
                    x = element.x
                    y = element.y
                }
                is PathQuadratic ->
                {
                    xs = quadratics(x, element.controlX, element.x, precision)
                    ys = quadratics(y, element.controlY, element.y, precision)

                    for (index in 1 until precision)
                    {
                        line = PathLine(xs[index - 1], ys[index - 1], 0f, xs[index], ys[index], 1f)
                        distance += line.distance
                        lines.add(line)
                    }

                    x = element.x
                    y = element.y
                }
                is PathCubic     ->
                {
                    xs = cubics(x, element.control1X, element.control2X, element.x, precision)
                    ys = cubics(y, element.control1Y, element.control2Y, element.y, precision)

                    for (index in 1 until precision)
                    {
                        line = PathLine(xs[index - 1], ys[index - 1], 0f, xs[index], ys[index], 1f)
                        distance += line.distance
                        lines.add(line)
                    }

                    x = element.x
                    y = element.y
                }
            }
        }

        if (isNul(distance))
        {
            return lines
        }

        var value = start
        val diff = end - start

        for (pathLine in lines)
        {
            pathLine.information1 = value
            value += (pathLine.distance * diff) / distance
            pathLine.information2 = value
        }

        return lines
    }
}
