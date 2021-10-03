package khelp.engine3d.geometry

import khelp.engine3d.utils.ThreadOpenGL
import khelp.utilities.serialization.ParsableSerializable
import khelp.utilities.serialization.Parser
import khelp.utilities.serialization.Serializer
import org.lwjgl.opengl.GL11
import java.util.Objects
import kotlin.math.sqrt

class Point2D(x : Float = 0f, y : Float = 0f) : ParsableSerializable
{
    companion object
    {
        fun provider() : Point2D = Point2D()
    }

    var x : Float = x
        private set
    var y : Float = y
        private set
    var length : Float = sqrt(this.x * this.x + this.y * this.y)
        private set

    operator fun plus(point2D : Point2D) : Point2D =
        Point2D(this.x + point2D.x, this.y + point2D.y)

    operator fun minus(point2D : Point2D) : Point2D =
        Point2D(this.x - point2D.x, this.y - point2D.y)

    operator fun times(factor : Float) : Point2D =
        Point2D(this.x * factor, this.y * factor)

    infix fun dot(point2D : Point2D) : Float =
        this.x * point2D.x + this.y * point2D.y

    infix fun cross(point2D : Point2D) : Float =
        this.y * point2D.x - this.x * point2D.y

    override fun hashCode() : Int = Objects.hash(this.x, this.y)

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is Point2D)
        {
            return false
        }

        return khelp.utilities.math.equals(this.x, other.x) && khelp.utilities.math.equals(this.y, other.y)
    }

    /**
     * Apply like UV in OpenGL
     */
    @ThreadOpenGL
    internal fun glTexCoord2f()
    {
        GL11.glTexCoord2f(this.x, this.y)
    }

    override fun serialize(serializer : Serializer)
    {
        serializer.setFloat("x", this.x)
        serializer.setFloat("y", this.y)
    }

    override fun parse(parser : Parser)
    {
        this.x = parser.getFloat("x")
        this.y = parser.getFloat("y")
        this.length = sqrt(this.x * this.x + this.y * this.y)
    }
}