package khelp.engine3d.geometry

import kotlin.math.max
import kotlin.math.min

class VirtualBox
{
    var empty : Boolean = true
        private set
    var minX : Float = Float.POSITIVE_INFINITY
        private set
    var maxX : Float = Float.NEGATIVE_INFINITY
        private set
    var minY : Float = Float.POSITIVE_INFINITY
        private set
    var maxY : Float = Float.NEGATIVE_INFINITY
        private set
    var minZ : Float = Float.POSITIVE_INFINITY
        private set
    var maxZ : Float = Float.NEGATIVE_INFINITY
        private set
    val center : Point3D
        get() = Point3D((this.maxX + this.minX) / 2f, (this.maxY + this.minY) / 2f, (this.maxZ + this.minZ) / 2f)

    fun add(x : Float, y : Float, z : Float)
    {
        this.minX = min(this.minX, x)
        this.maxX = max(this.maxX, x)
        this.minY = min(this.minY, y)
        this.maxY = max(this.maxY, y)
        this.minZ = min(this.minZ, z)
        this.maxZ = max(this.maxZ, z)
        this.empty = false
    }

    fun add(point : Point3D)
    {
        this.add(point.x, point.y, point.z)
    }

    fun add(virtualBox : VirtualBox)
    {
        if (virtualBox.empty)
        {
            return
        }

        this.add(virtualBox.minX, virtualBox.minY, virtualBox.minZ)
        this.add(virtualBox.maxX, virtualBox.maxY, virtualBox.maxZ)
    }

    fun copy() : VirtualBox
    {
        val copy = VirtualBox()
        copy.empty = this.empty
        copy.minX = this.minX
        copy.maxX = this.maxX
        copy.minY = this.minY
        copy.maxY = this.maxY
        copy.minZ = this.minZ
        copy.maxZ = this.maxZ
        return copy
    }

    fun contains(x : Float, y : Float, z : Float) : Boolean =
        x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY && z >= this.minZ && z <= this.maxZ

    fun interercts(virtualBox : VirtualBox) : Boolean
    {
        if (this.minX > virtualBox.maxX || this.minY > virtualBox.maxY || this.minZ > virtualBox.maxZ
            || this.maxX < virtualBox.minX || this.maxY < virtualBox.minY || this.maxZ < virtualBox.minZ)
        {
            return false
        }

        val xMin = max(this.minX, virtualBox.minX)
        val xMax = min(this.maxX, virtualBox.maxX)

        if (xMin >= xMax)
        {
            return false
        }

        val yMin = max(this.minY, virtualBox.minY)
        val yMax = min(this.maxY, virtualBox.maxY)

        if (yMin >= yMax)
        {
            return false
        }


        val zMin = max(this.minZ, virtualBox.minZ)
        val zMax = min(this.maxZ, virtualBox.maxZ)

        return zMin < zMax
    }

    override fun toString() : String =
        "Box (${this.minX}, ${this.minY}, ${this.minZ}) x (${this.maxX}, ${this.maxY}, ${this.maxZ})"

    fun reset()
    {
        this.empty = true
        this.minX = Float.POSITIVE_INFINITY
        this.maxX = Float.NEGATIVE_INFINITY
        this.minY = Float.POSITIVE_INFINITY
        this.maxY = Float.NEGATIVE_INFINITY
        this.minZ = Float.POSITIVE_INFINITY
        this.maxZ = Float.NEGATIVE_INFINITY
    }
}
