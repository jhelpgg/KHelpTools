package khelp.engine3d.shader

import khelp.engine3d.render.versionOpenGL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

sealed class Shader(val code : String, private val shaderType : Int, val minimumOpenGL : Int)
{
    private var error : Exception? = null
    private var reference : Int = -1

    fun compute() : Result<Unit>
    {
        if (this.code.isEmpty())
        {
            return Result.success(Unit)
        }

        this.error?.let { error -> return Result.failure(error) }

        if (versionOpenGL < this.minimumOpenGL)
        {
            this.error = OpenGLVersionToOldException(this.minimumOpenGL)
            return Result.failure(this.error!!)
        }

        if (this.reference >= 0)
        {
            return Result.success(Unit)
        }

        this.reference = GL20.glCreateShader(this.shaderType)
        GL20.glShaderSource(this.reference, this.code)
        GL20.glCompileShader(this.reference)
        val success = IntArray(1)
        GL20.glGetShaderiv(this.reference, GL20.GL_COMPILE_STATUS, success)

        if (success[0] == GL11.GL_FALSE)
        {
            GL20.glDeleteShader(this.reference)
            this.reference = -1
            this.error = ShaderCompilationException(GL20.glGetShaderInfoLog(this.reference))
            return Result.failure(this.error!!)
        }

        return Result.success(Unit)
    }

    internal fun attachToProgram(programReference : Int)
    {
        val compute = this.compute()

        if (compute.isFailure)
        {
            println("Warning ${this.javaClass.simpleName}, not attached because : ${compute.exceptionOrNull()}")
            return
        }

        if (this.reference < 0)
        {
            return
        }

        GL20.glAttachShader(programReference, this.reference)
    }

    internal fun delete()
    {
        if (this.reference < 0)
        {
            return
        }

        GL20.glDeleteShader(this.reference)
        this.reference = -1
    }
}