package khelp.engine3d.render.font

import khelp.ui.utilities.AFFINE_TRANSFORM
import khelp.ui.utilities.FLATNESS
import khelp.utilities.math.square
import java.awt.Shape
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class BorderIterator(shape:Shape) : Iterable<BorderElement>
{
    private val elements = ArrayList<BorderElement>()
    val windingRule : WindingRule
    val length : Float
    val minX : Float
    val maxX : Float
    val minY : Float
    val maxY : Float

    init
    {
        val pathIterator = shape.getPathIterator(AFFINE_TRANSFORM, FLATNESS)
        this.windingRule = windingRule(pathIterator.windingRule)
        val coordinates = FloatArray(6)
        var length = 0f
        var first = true
        var dx = 0f
        var dy = 0f
        var cx = 0f
        var cy = 0f
        var fx = 0f
        var fy = 0f
        var minX = 0f
        var maxX = 0f
        var minY = 0f
        var maxY = 0f

        while(!pathIterator.isDone)
        {
            val type = pathIterator.currentSegment(coordinates)
            val borderElement = BorderElement(borderElementType(type), coordinates[0], coordinates[1])
            this.elements.add(borderElement)

            when(borderElement.borderElementType)
            {
                BorderElementType.MOVE ->
                {
                    dx = borderElement.x
                    dy = borderElement.y
                    cx = borderElement.x
                    cy = borderElement.y
                }
                BorderElementType.LINE ->
                {
                    fx = borderElement.x
                    fy = borderElement.y
                    length += sqrt(square(cx-fx) + square(cy-fy))
                    cx =fx
                    cy = fy
                }
                BorderElementType.CLOSE ->
                {
                    length += sqrt(square(cx-dx) + square(cy-dy))
                    cx =dx
                    cy = dy
                }
            }

            if(first)
            {
                minX = borderElement.x
                maxX = borderElement.x
                minY = borderElement.y
                maxY = borderElement.y
                first = false
            }
            else
            {
                minX = min(minX, borderElement.x)
                maxX = max(maxX, borderElement.x)
                minY = min(minY, borderElement.y)
                maxY = max(maxY, borderElement.y)
            }

            pathIterator.next()
        }

        this.length = length
        this.minX = minX
        this.maxX = maxX
        this.minY = minY
        this.maxY = maxY
    }

    override fun iterator() : Iterator<BorderElement> = this.elements.iterator()
}