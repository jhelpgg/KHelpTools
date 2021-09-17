package khelp.engine3d.render

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWErrorCallbackI
import java.lang.reflect.Field
import java.util.function.BiPredicate
import org.lwjgl.system.APIUtil


/**
 * Map of error codes and their name
 */
private val ERROR_CODES = APIUtil.apiClassTokens(
    { _, value -> value in 0x10001 .. 0x1ffff },
    null,
    GLFW::class.java)

object DebugGLFErrorCallback : GLFWErrorCallbackI
{
    override fun invoke(error: Int, description: Long)
    {
        val errorType = ERROR_CODES[error]
        val message = GLFWErrorCallback.getDescription(description)
        khelp.utilities.log.error("GLF -", errorType, "- ", message)
    }
}