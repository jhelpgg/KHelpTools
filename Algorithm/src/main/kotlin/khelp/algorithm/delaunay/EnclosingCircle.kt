package khelp.algorithm.delaunay

import khelp.utilities.math.square
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class EnclosingCircle
{
    var empty : Boolean = true
        private set
    var centerX : Float = Float.NaN
        private set
    var centerY : Float = Float.NaN
        private set
    var ray : Float = Float.NaN
        private set

    private var minX = Float.POSITIVE_INFINITY
    private var minY = Float.POSITIVE_INFINITY
    private var maxX = Float.NEGATIVE_INFINITY
    private var maxY = Float.NEGATIVE_INFINITY

    fun addPoint(x : Float, y : Float)
    {
        if (this.empty)
        {
            this.empty = false
            this.centerX = x
            this.centerY = y
            this.minX = x
            this.minY = y
            this.maxX = x
            this.maxY = y
            this.ray = 0f
            return
        }

        this.minX = min(this.minX, x)
        this.minY = min(this.minY, y)
        this.maxX = max(this.maxX, x)
        this.maxY = max(this.maxY, y)
        this.centerX = (this.maxX + this.minX) / 2f
        this.centerY = (this.maxY + this.minY) / 2f
        this.ray = sqrt(square(this.maxX - this.minX) + square(this.maxY - this.minY)) / 2f
    }
}