package khelp.engine3d.font

import java.awt.Shape
import khelp.algorithm.delaunay.Delaunay
import khelp.engine3d.extensions.PI_FLOAT
import khelp.utilities.math.square
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

internal class DelaunayFiller(private val shape : Shape, private val delaunay : Delaunay)
{
    private var firstX = 0f
    private var firstY = 0f
    private var x1 = 0f
    private var x2 = 0f
    private var y1 = 0f
    private var y2 = 0f

    fun moveTo(x : Float, y : Float)
    {
        this.delaunay.addPoint(x, y)
        this.firstX = x
        this.firstY = y
        this.x1 = x
        this.y1 = y
    }

    fun lineTo(x : Float, y : Float)
    {
        this.delaunay.addPoint(x, y)
        this.x2 = x
        this.y2 = y
        this.addIgnorePoint()
        this.x1 = x2
        this.y1 = y2
    }

    fun close()
    {
        this.x2 = firstX
        this.y2 = firstY
        this.addIgnorePoint()
    }

    private fun addIgnorePoint()
    {
        val centerX = (this.x2 + this.x1) / 2f
        val centerY = (this.y2 + this.y1) / 2f
        val ray = sqrt(square(centerX - this.x1) + square(centerY - this.y1)) * 1.1f
        val angle = atan2(this.y2 - this.y1, this.x2 - this.x1) + PI_FLOAT / 2f
        var px = centerX + ray * cos(angle)
        var py = centerY + ray * sin(angle)

        if (shape.contains(px.toDouble(), py.toDouble()))
        {
            px = this.x2 * 2f - px
            py = this.y2 * 2f - py

            if (this.shape.contains(px.toDouble(), py.toDouble()).not())
            {
                this.delaunay.addIgnorePoint(px, py)
            }
        }
        else
        {
            this.delaunay.addIgnorePoint(px, py)
        }
    }
}