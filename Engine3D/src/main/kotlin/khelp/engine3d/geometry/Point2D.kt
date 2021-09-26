package khelp.engine3d.geometry

import khelp.engine3d.utils.ThreadOpenGL
import org.lwjgl.opengl.GL11
import java.util.Objects
import kotlin.math.sqrt

data class Point2D(val x : Float = 0f, val y : Float = 0f)
{
    val length : Float = sqrt(this.x * this.x + this.y * this.y)

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
}