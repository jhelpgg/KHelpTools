package khelp.editor.ui.mesheditor.editable

import khelp.engine3d.geometry.Point2D
import khelp.engine3d.geometry.Point3D
import khelp.engine3d.geometry.Vec3f
import kotlin.math.max
import kotlin.math.min

class ComputeUVAndNormal
{
    companion object
    {
        private const val MINIMUM = 1e-6f
    }

    private var minX = Float.POSITIVE_INFINITY
    private var maxX = Float.NEGATIVE_INFINITY
    private var minY = Float.POSITIVE_INFINITY
    private var maxY = Float.NEGATIVE_INFINITY
    private var minZ = Float.POSITIVE_INFINITY
    private var maxZ = Float.NEGATIVE_INFINITY
    private var diffX = 1f
    private var diffY = 1f
    private var diffZ = 1f
    private var typeUV = TypeUV.UNDEFINED
    private var firstPoint : Vec3f? = null
    private var secondPoint : Vec3f? = null
    private var thirdPoint : Vec3f? = null
    var normal = Point3D()
        private set

    fun add(point : Point3D)
    {
        this.minX = min(this.minX, point.x)
        this.maxX = max(this.maxX, point.x)
        this.minY = min(this.minY, point.y)
        this.maxY = max(this.maxY, point.y)
        this.minZ = min(this.minZ, point.z)
        this.maxZ = max(this.maxZ, point.z)

        this.diffX = max(ComputeUVAndNormal.MINIMUM, this.maxX - this.minX)
        this.diffY = max(ComputeUVAndNormal.MINIMUM, this.maxY - this.minY)
        this.diffZ = max(ComputeUVAndNormal.MINIMUM, this.maxZ - this.minZ)

        this.typeUV =
            when
            {
                this.diffX >= this.diffZ && this.diffY >= this.diffZ -> TypeUV.XY
                this.diffX >= this.diffY && this.diffZ >= this.diffY -> TypeUV.XZ
                this.diffY >= this.diffX && this.diffZ >= this.diffX -> TypeUV.YZ
                else                                                 -> TypeUV.UNDEFINED
            }

        if (this.firstPoint == null)
        {
            this.firstPoint = Vec3f(point)
        }
        else if (this.secondPoint == null)
        {
            this.secondPoint = Vec3f(point)
        }
        else if (this.thirdPoint == null)
        {
            this.thirdPoint = Vec3f(point)
            val vect1 = this.firstPoint !! - this.secondPoint !!
            val vect2 = this.thirdPoint !! - this.secondPoint !!
            val normal = vect1.cross(vect2)
            normal.normalize()
            this.normal = Point3D(normal)
        }
    }

    fun computeUV(point : Point3D) : Point2D =
        when (this.typeUV)
        {
            TypeUV.XY -> Point2D((point.x - this.minX) / this.diffX, (point.y - this.minY) / this.diffY)
            TypeUV.XZ -> Point2D((point.x - this.minX) / this.diffX, (point.z - this.minZ) / this.diffZ)
            TypeUV.YZ -> Point2D((point.y - this.minY) / this.diffY, (point.z - this.minZ) / this.diffZ)
            else      -> Point2D(0f, 0f)
        }
}