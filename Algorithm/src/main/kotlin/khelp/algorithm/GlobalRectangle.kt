package khelp.algorithm

import java.awt.geom.Rectangle2D
import kotlin.math.max
import kotlin.math.min

class GlobalRectangle(vararg points : PointF)
{
    val rectangle : Rectangle2D.Float
        get() =
            if (this.empty) Rectangle2D.Float(0f, 0f, 0f, 0f)
            else Rectangle2D.Float(this.minX, this.minY, this.maxX - this.minX, this.maxY - this.minY)
    var empty = true
        private set
    var minX = Float.POSITIVE_INFINITY
        private set
    var maxX = Float.NEGATIVE_INFINITY
        private set
    var minY = Float.POSITIVE_INFINITY
        private set
    var maxY = Float.NEGATIVE_INFINITY
        private set

    val width : Float get() = if (this.empty) Float.NaN else this.maxX - this.minX
    val height : Float get() = if (this.empty) Float.NaN else this.maxY - this.minY

    init
    {
        this.add(*points)
    }

    fun add(x : Float, y : Float)
    {
        this.empty = false
        this.minX = min(this.minX, x)
        this.maxX = max(this.maxX, x)
        this.minY = min(this.minY, y)
        this.maxY = max(this.maxY, y)
    }

    fun add(vararg points : PointF)
    {
        for (point in points)
        {
            this.add(point.x, point.y)
        }
    }
}