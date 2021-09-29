package khelp.engine3d.geometry

import khelp.engine3d.utils.ThreadOpenGL
import org.lwjgl.opengl.GL11

data class Point3D(val x : Float = 0f, val y : Float = 0f, val z : Float = 0f)
{
    constructor(vect3f : Vec3f) : this(vect3f.x, vect3f.y, vect3f.z)

    fun toVect3f() = Vec3f(this.x, this.y, this.z)

    operator fun plus(point3D : Point3D) : Point3D =
        Point3D(this.x + point3D.x, this.y + point3D.y, this.z + point3D.z)

    operator fun plus(vec3f : Vec3f) : Point3D =
        Point3D(this.x + vec3f.x, this.y + vec3f.y, this.z + vec3f.z)

    fun add(x : Float, y : Float, z : Float) : Point3D =
        Point3D(this.x + x, this.y + y, this.z + z)

    operator fun times(factor : Float) : Point3D =
        Point3D(factor * this.x, factor * this.y, factor * this.z)

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is Point3D)
        {
            return false
        }

        return khelp.utilities.math.equals(this.x, other.x)
               && khelp.utilities.math.equals(this.y, other.y)
               && khelp.utilities.math.equals(this.z, other.z)
    }

    @ThreadOpenGL
    internal fun glVertex3f()
    {
        GL11.glVertex3f(this.x, this.y, this.z)
    }

    @ThreadOpenGL
    internal fun glNormal3f()
    {
        GL11.glNormal3f(this.x, this.y, this.z)
    }
}
