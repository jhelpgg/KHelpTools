package khelp.algorithm.interpolation.grid

import java.awt.Shape
import khelp.algorithm.GlobalRectangle
import khelp.algorithm.PointF
import khelp.algorithm.interpolation.ColorScale
import khelp.algorithm.interpolation.InterpolationImage
import khelp.thread.Mutex
import khelp.utilities.math.isNul
import khelp.utilities.math.square
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class InterpolationDistanceGrid(width : Int, height : Int, activeZones : Array<PointF>,
                                var colorScale : ColorScale, defaultValue : Float = 0f,
                                private val shape : Shape = GlobalRectangle(*activeZones).rectangle)
    : InterpolationImage(width, height)
{
    val numberZone = activeZones.size
    private val values = FloatArray(activeZones.size) { defaultValue }
    private val grid = Array<FloatArray>(width * height) { FloatArray(activeZones.size) }
    private val mutex = Mutex()

    init
    {
        val rectangle = GlobalRectangle(*activeZones)
        val rectangle2 = this.shape.bounds2D
        val minX = min(rectangle.minX, rectangle2.minX.toFloat())
        val factorWidth = max(rectangle.width, rectangle2.width.toFloat()) / width
        val minY = min(rectangle.minY, rectangle2.minY.toFloat())
        val factorHeight = max(rectangle.height, rectangle2.height.toFloat()) / height

        var index = 0

        for (y in 0 until height)
        {
            val yy = y * factorHeight + minY

            for (x in 0 until width)
            {
                val xx = x * factorWidth + minX

                if (this.shape.contains(xx.toDouble(), yy.toDouble()))
                {
                    var maxDistance = 0f
                    var totalDistance = 0f
                    val distances = FloatArray(activeZones.size) { indexZone ->
                        val point = activeZones[indexZone]
                        val distance = sqrt(square(xx - point.x) + square(yy - point.y))
                        maxDistance = max(maxDistance, distance)
                        totalDistance += distance
                        distance
                    }

                    for (indexDistance in distances.indices)
                    {
                        distances[indexDistance] = maxDistance - distances[indexDistance]
                    }

                    totalDistance = distances.sum()

                    if (isNul(totalDistance))
                    {
                        totalDistance = 1f
                    }

                    for (indexDistance in distances.indices)
                    {
                        distances[indexDistance] = distances[indexDistance] / totalDistance
                    }

                    this.grid[index] = distances
                }
                else
                {
                    this.grid[index] = floatArrayOf()
                }

                index++
            }
        }

        this.refresh()
    }

    fun value(index : Int, value : Float)
    {
        this.mutex {
            this.values[index] = value
            this.refresh()
        }
    }

    fun values(vararg values : Float)
    {
        this.values(0, *values)
    }

    fun values(startIndex : Int, vararg values : Float)
    {
        this.mutex {
            val start = max(0, startIndex)
            val max = min(this.numberZone - start, values.size)
            System.arraycopy(values, 0, this.values, start, max)
            this.refresh()
        }
    }

    private fun refresh()
    {
        val pixels = this.image.pixels(0, 0, this.width, this.height)

        for (index in pixels.indices)
        {
            val coefficients = this.grid[index]

            pixels[index] =
                if (coefficients.isEmpty())
                {
                    0
                }
                else
                {
                    var value = 0f

                    for ((indexCoefficient, coefficient) in coefficients.withIndex())
                    {
                        value += coefficient * this.values[indexCoefficient]
                    }

                    this.colorScale.color(value)
                }
        }

        this.image.startDrawMode()
        this.image.pixels(0, 0, this.width, this.height, pixels)
        this.image.endDrawMode()
        this.update()
    }
}