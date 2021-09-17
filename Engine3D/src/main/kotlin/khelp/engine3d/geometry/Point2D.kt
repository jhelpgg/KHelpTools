package khelp.engine3d.geometry

import khelp.engine3d.utils.ThreadOpenGL
import org.lwjgl.opengl.GL11

data class Point2D(val x : Float = 0f, val y : Float = 0f)
{
    /**
     * Apply like UV in OpenGL
     */
    @ThreadOpenGL
    internal fun glTexCoord2f()
    {
        GL11.glTexCoord2f(this.x, this.y)
    }
}