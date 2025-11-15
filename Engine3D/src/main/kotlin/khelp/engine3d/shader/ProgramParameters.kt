package khelp.engine3d.shader

import khelp.engine3d.render.versionOpenGL
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL32

class ProgramParameters
{
    private val reference : Int by lazy {
        if(versionOpenGL<30)
        {
            -1
        }
        else
        {
            val buffer = IntArray(1)
            GL30.glGenVertexArrays(buffer)
            buffer[0]
        }
    }


}