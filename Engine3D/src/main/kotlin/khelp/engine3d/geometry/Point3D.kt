package khelp.engine3d.geometry

import khelp.engine3d.utils.ThreadOpenGL
import khelp.utilities.serialization.ParsableSerializable
import khelp.utilities.serialization.Parser
import khelp.utilities.serialization.Serializer
import org.lwjgl.opengl.GL11
import java.util.Objects

class Point3D(x : Float = 0f, y : Float = 0f, z : Float = 0f) : ParsableSerializable
{
    companion object
    {
        fun provider() : Point3D = Point3D()
    }

    var x = x
        private set
    var y = y
        private set
    var z = z
        private set

    constructor(vect3f : Vec3f) : this(vect3f.x, vect3f.y, vect3f.z)

    fun toVect3f() = Vec3f(this.x, this.y, this.z)

    operator fun plus(point3D : Point3D) : Point3D =
        Point3D(this.x + point3D.x, this.y + point3D.y, this.z + point3D.z)

    operator fun plus(vec3f : Vec3f) : Point3D =
        Point3D(this.x + vec3f.x, this.y + vec3f.y, this.z + vec3f.z)

    fun add(x : Float, y : Float, z : Float) : Point3D =
        Point3D(this.x + x, this.y + y, this.z + z)

    fun translate(x : Float, y : Float, z : Float)
    {
        this.x += x
        this.y += y
        this.z += z
    }

    operator fun times(factor : Float) : Point3D =
        Point3D(factor * this.x, factor * this.y, factor * this.z)

    override fun hashCode() : Int = Objects.hash(this.x, this.y, this.z)

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

    fun copy() : Point3D = Point3D(this.x, this.y, this.z)

    fun middle(point3D:Point3D) : Point3D = Point3D((this.x+point3D.x)/2f,
                                                    (this.y+point3D.y)/2f,
                                                    (this.z+point3D.z)/2f)

    override fun toString() : String = "(${this.x}, ${this.y}, ${this.z})"

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

    override fun serialize(serializer : Serializer)
    {
        serializer.setFloat("x", this.x)
        serializer.setFloat("y", this.y)
        serializer.setFloat("z", this.z)
    }

    override fun parse(parser : Parser)
    {
        this.x = parser.getFloat("x")
        this.y = parser.getFloat("y")
        this.z = parser.getFloat("z")
    }
}
