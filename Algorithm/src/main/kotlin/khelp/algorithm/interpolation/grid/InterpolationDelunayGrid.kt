package khelp.algorithm.interpolation.grid

import khelp.algorithm.GlobalRectangle
import khelp.algorithm.PointF
import khelp.algorithm.delaunay.Delaunay
import khelp.algorithm.delaunay.TriangleIndexed
import khelp.algorithm.interpolation.ColorScale
import khelp.algorithm.interpolation.InterpolationImage
import khelp.algorithm.interpolation.grid.cell.GridCell
import khelp.algorithm.interpolation.grid.cell.GridCellCoefficients
import khelp.algorithm.interpolation.grid.cell.GridCellEmpty
import khelp.thread.Mutex
import kotlin.math.max
import kotlin.math.min

class InterpolationDelunayGrid(width : Int, height : Int, activeZones : Array<PointF>,
                               var colorScale : ColorScale, defaultValue : Float = 0f)
    : InterpolationImage(width, height)
{
    val numberZone = activeZones.size

    private val values = FloatArray(activeZones.size) { defaultValue }
    private val grid = Array<GridCell>(width * height) { GridCellEmpty }
    private val mutex = Mutex()

    init
    {
        val rectangle = GlobalRectangle(*activeZones)
        val delaunay = Delaunay()

        for (point in activeZones)
        {
            delaunay.addPoint(point.x, point.y)
        }

        val minX = rectangle.minX
        val factorWidth = rectangle.width / width
        val minY = rectangle.minY
        val factorHeight = rectangle.height / height
        val triangles = delaunay.triangles()

        val getTriangle : (x : Int, y : Int) -> Pair<TriangleIndexed, PointF>? =
            { x, y ->
                val xx = x * factorWidth + minX
                val yy = y * factorHeight + minY
                var pairFound : Pair<TriangleIndexed, PointF>? = null

                for (triangle in triangles)
                {
                    if (triangle.contains(xx, yy))
                    {
                        pairFound = Pair(triangle, PointF(xx, yy))
                        break
                    }
                }

                pairFound
            }

        var index = 0

        for (y in 0 until height)
        {
            for (x in 0 until width)
            {
                val pair = getTriangle(x, y)

                if (pair != null)
                {
                    val (triangle, point) = pair
                    val coefficients = triangle.coefficients(point.x, point.y)
                    this.grid[index] = GridCellCoefficients(triangle.point1.index, coefficients.coefficientOnPoint1,
                                                            triangle.point2.index, coefficients.coefficientOnPoint2,
                                                            triangle.point3.index, coefficients.coefficientOnPoint3)
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
            val gridCell = this.grid[index]

            pixels[index] =
                when (gridCell)
                {
                    is GridCellEmpty        -> 0

                    is GridCellCoefficients ->
                        this.colorScale.color(this.values[gridCell.pointIndex1] * gridCell.coefficient1 +
                                              this.values[gridCell.pointIndex2] * gridCell.coefficient2 +
                                              this.values[gridCell.pointIndex3] * gridCell.coefficient3)
                }
        }

        this.image.startDrawMode()
        this.image.pixels(0, 0, this.width, this.height, pixels)
        this.image.endDrawMode()
        this.update()
    }
}