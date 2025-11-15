package khelp.engine3d.render

import org.lwjgl.opengl.GLCapabilities

var versionOpenGL = 11
    private set

private lateinit var openGlCapabilities : GLCapabilities

internal fun glCapabilities(glCapabilities : GLCapabilities)
{
    openGlCapabilities = glCapabilities

    versionOpenGL =
        when
        {
            glCapabilities.OpenGL46 -> 46
            glCapabilities.OpenGL45 -> 45
            glCapabilities.OpenGL44 -> 44
            glCapabilities.OpenGL43 -> 43
            glCapabilities.OpenGL42 -> 42
            glCapabilities.OpenGL41 -> 41
            glCapabilities.OpenGL40 -> 40
            glCapabilities.OpenGL33 -> 33
            glCapabilities.OpenGL32 -> 32
            glCapabilities.OpenGL31 -> 31
            glCapabilities.OpenGL30 -> 30
            glCapabilities.OpenGL21 -> 21
            glCapabilities.OpenGL20 -> 20
            glCapabilities.OpenGL15 -> 15
            glCapabilities.OpenGL14 -> 14
            glCapabilities.OpenGL13 -> 13
            glCapabilities.OpenGL12 -> 12
            else                    -> 11
        }
}
