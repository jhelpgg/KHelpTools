package khelp.engine3d.shader

import khelp.engine3d.render.versionOpenGL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

class Program
{
    companion object
    {
        private val defaultVertexShader = VertexShader("")
        private val defaultGeometryShader = GeometryShader("")
        private val defaultFragmentShader = FragmentShader("")
    }

    var vertexShader : VertexShader = Program.defaultVertexShader
    var geometryShader : GeometryShader = Program.defaultGeometryShader
    var fragmentShader : FragmentShader = Program.defaultFragmentShader

    private var reference = -1

    fun compile() : Result<Unit>
    {
        if(versionOpenGL < 20)
        {
            return Result.failure(OpenGLVersionToOldException(20))
        }

        if (this.reference >= 0)
        {
            return Result.success(Unit)
        }

        this.reference = GL20.glCreateProgram()
        this.vertexShader.attachToProgram(this.reference)
        this.geometryShader.attachToProgram(this.reference)
        this.fragmentShader.attachToProgram(this.reference)
        GL20.glLinkProgram(this.reference)

        val success = IntArray(1)
        GL20.glGetProgramiv(this.reference, GL20.GL_LINK_STATUS, success)

        if (success[0] == GL11.GL_FALSE)
        {
            GL20.glDeleteProgram(this.reference)
            this.reference = -1
            return Result.failure(ProgramCompilationException(GL20.glGetShaderInfoLog(this.reference)))
        }

        return Result.success(Unit)
    }

    internal fun useProgram()
    {
        val result = this.compile()

        if (result.isFailure)
        {
            println("Waring program not compiled : ${this.compile()}")
        }

        GL20.glUseProgram(this.reference)
    }

    internal fun delete(deleteAlsoShaders : Boolean = true)
    {
        if (this.reference >= 0)
        {
            GL20.glDeleteProgram(this.reference)
            this.reference = -1

            if (deleteAlsoShaders)
            {
                this.vertexShader.delete()
                this.geometryShader.delete()
                this.fragmentShader.delete()
                this.vertexShader = Program.defaultVertexShader
                this.geometryShader = Program.defaultGeometryShader
                this.fragmentShader = Program.defaultFragmentShader
            }
        }
    }
}