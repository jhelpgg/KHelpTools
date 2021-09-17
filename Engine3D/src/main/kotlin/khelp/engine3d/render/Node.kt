package khelp.engine3d.render

import khelp.utilities.extensions.bounds
import java.util.Vector
import kotlin.math.max
import kotlin.math.min

open class Node : Iterable<Node>
{
    private var limitMinX = Float.NEGATIVE_INFINITY
    private var limitMaxX = Float.POSITIVE_INFINITY
    private var limitMinY = Float.NEGATIVE_INFINITY
    private var limitMaxY = Float.POSITIVE_INFINITY
    private var limitMinZ = Float.NEGATIVE_INFINITY
    private var limitMaxZ = Float.POSITIVE_INFINITY

    private var limitMinAngleX = Float.NEGATIVE_INFINITY
    private var limitMaxAngleX = Float.POSITIVE_INFINITY
    private var limitMinAngleY = Float.NEGATIVE_INFINITY
    private var limitMaxAngleY = Float.POSITIVE_INFINITY
    private var limitMinAngleZ = Float.NEGATIVE_INFINITY
    private var limitMaxAngleZ = Float.POSITIVE_INFINITY

    private var limitMinZoomX = Float.NEGATIVE_INFINITY
    private var limitMaxZoomX = Float.POSITIVE_INFINITY
    private var limitMinZoomY = Float.NEGATIVE_INFINITY
    private var limitMaxZoomY = Float.POSITIVE_INFINITY
    private var limitMinZoomZ = Float.NEGATIVE_INFINITY
    private var limitMaxZoomZ = Float.POSITIVE_INFINITY

    var x : Float = 0f
        set(value)
        {
            field = value.bounds(this.limitMinX, this.limitMaxX)
        }
    var y : Float = 0f
        set(value)
        {
            field = value.bounds(this.limitMinY, this.limitMaxY)
        }
    var z : Float = 0f
        set(value)
        {
            field = value.bounds(this.limitMinZ, this.limitMaxZ)
        }

    var angleX : Float = 0f
        set(value)
        {
            field = value.bounds(this.limitMinAngleX, this.limitMaxAngleX)
        }
    var angleY : Float = 0f
        set(value)
        {
            field = value.bounds(this.limitMinAngleY, this.limitMaxAngleY)
        }
    var angleZ : Float = 0f
        set(value)
        {
            field = value.bounds(this.limitMinAngleZ, this.limitMaxAngleZ)
        }

    var zoomX : Float = 1f
        set(value)
        {
            field = value.bounds(this.limitMinZoomX, this.limitMaxZoomX)
        }
    var zoomY : Float = 1f
        set(value)
        {
            field = value.bounds(this.limitMinZoomY, this.limitMaxZoomY)
        }
    var zoomZ : Float = 1f
        set(value)
        {
            field = value.bounds(this.limitMinZoomZ, this.limitMaxZoomZ)
        }

    private val children = Vector<Node>(8)


    fun limitX(limit1 : Float, limit2 : Float)
    {
        this.limitMinX = min(limit1, limit2)
        this.limitMaxX = max(limit1, limit2)
        this.x = this.x.bounds(this.limitMinX, this.limitMaxX)
    }

    fun limitY(limit1 : Float, limit2 : Float)
    {
        this.limitMinY = min(limit1, limit2)
        this.limitMaxY = max(limit1, limit2)
        this.y = this.y.bounds(this.limitMinY, this.limitMaxY)
    }

    fun limitZ(limit1 : Float, limit2 : Float)
    {
        this.limitMinZ = min(limit1, limit2)
        this.limitMaxZ = max(limit1, limit2)
        this.z = this.z.bounds(this.limitMinZ, this.limitMaxZ)
    }

    fun limitAngleX(limit1 : Float, limit2 : Float)
    {
        this.limitMinAngleX = min(limit1, limit2)
        this.limitMaxAngleX = max(limit1, limit2)
        this.angleX = this.angleX.bounds(this.limitMinAngleX, this.limitMaxAngleX)
    }

    fun limitAngleY(limit1 : Float, limit2 : Float)
    {
        this.limitMinAngleY = min(limit1, limit2)
        this.limitMaxAngleY = max(limit1, limit2)
        this.angleY = this.angleY.bounds(this.limitMinAngleY, this.limitMaxAngleY)
    }

    fun limitAngleZ(limit1 : Float, limit2 : Float)
    {
        this.limitMinAngleZ = min(limit1, limit2)
        this.limitMaxAngleZ = max(limit1, limit2)
        this.angleZ = this.angleZ.bounds(this.limitMinAngleZ, this.limitMaxAngleZ)
    }

    fun limitZoomX(limit1 : Float, limit2 : Float)
    {
        this.limitMinZoomX = min(limit1, limit2)
        this.limitMaxZoomX = max(limit1, limit2)
        this.zoomX = this.zoomX.bounds(this.limitMinZoomX, this.limitMaxZoomX)
    }

    fun limitZoomY(limit1 : Float, limit2 : Float)
    {
        this.limitMinZoomY = min(limit1, limit2)
        this.limitMaxZoomY = max(limit1, limit2)
        this.zoomY = this.zoomY.bounds(this.limitMinZoomY, this.limitMaxZoomY)
    }

    fun limitZoomZ(limit1 : Float, limit2 : Float)
    {
        this.limitMinZoomZ = min(limit1, limit2)
        this.limitMaxZoomZ = max(limit1, limit2)
        this.zoomZ = this.zoomZ.bounds(this.limitMinZoomZ, this.limitMaxZoomZ)
    }

    fun freeX()
    {
        this.limitMinX = Float.NEGATIVE_INFINITY
        this.limitMaxX = Float.POSITIVE_INFINITY
    }

    fun freeY()
    {
        this.limitMinY = Float.NEGATIVE_INFINITY
        this.limitMaxY = Float.POSITIVE_INFINITY
    }

    fun freeZ()
    {
        this.limitMinZ = Float.NEGATIVE_INFINITY
        this.limitMaxZ = Float.POSITIVE_INFINITY
    }

    fun freeAngleX()
    {
        this.limitMinAngleX = Float.NEGATIVE_INFINITY
        this.limitMaxAngleX = Float.POSITIVE_INFINITY
    }

    fun freeAngleY()
    {
        this.limitMinAngleY = Float.NEGATIVE_INFINITY
        this.limitMaxAngleY = Float.POSITIVE_INFINITY
    }

    fun freeAngleZ()
    {
        this.limitMinAngleZ = Float.NEGATIVE_INFINITY
        this.limitMaxAngleZ = Float.POSITIVE_INFINITY
    }

    fun freeZoomX()
    {
        this.limitMinZoomX = Float.NEGATIVE_INFINITY
        this.limitMaxZoomX = Float.POSITIVE_INFINITY
    }

    fun freeZoomY()
    {
        this.limitMinZoomY = Float.NEGATIVE_INFINITY
        this.limitMaxZoomY = Float.POSITIVE_INFINITY
    }

    fun freeZoomZ()
    {
        this.limitMinZoomZ = Float.NEGATIVE_INFINITY
        this.limitMaxZoomZ = Float.POSITIVE_INFINITY
    }

    override fun iterator() : Iterator<Node> = this.children.iterator()
}
