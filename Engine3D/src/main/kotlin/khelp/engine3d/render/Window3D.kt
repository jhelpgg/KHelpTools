package khelp.engine3d.render

import khelp.io.ExternalSource
import khelp.preferences.Preferences
import khelp.resources.Resources
import khelp.thread.Locker
import khelp.thread.parallel
import khelp.utilities.log.warning
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWCursorEnterCallbackI
import org.lwjgl.glfw.GLFWCursorPosCallbackI
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWJoystickCallbackI
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.glfw.GLFWMouseButtonCallbackI
import org.lwjgl.glfw.GLFWWindowCloseCallbackI
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil


@WindowDSL
fun window3D(width : Int, height : Int, title : String, decorated : Boolean = true, windowCreator : Window3D.() -> Unit)
{
    val window3D = Window3D.createWindow3D(width, height, title, decorated, false)
    windowCreator(window3D)
    window3D.waitWindowClose()
}

@WindowDSL
fun window3DFull(title : String, windowCreator : Window3D.() -> Unit)
{
    val window3D = Window3D.createWindow3D(800, 600, title, false, true)
    windowCreator(window3D)
    window3D.waitWindowClose()
}


class Window3D private constructor()
{
    companion object
    {
        internal fun createWindow3D(requestWidth : Int, requestHeight : Int, title : String,
                                    decorated : Boolean, maximized : Boolean) : Window3D
        {
            val window3D = Window3D()

            parallel {
                var width = requestWidth
                var height = requestHeight

                // Setup the error callback.
                GLFWErrorCallback.create(DebugGLFErrorCallback)
                    .set()

                // Initialize GLFW. Most GLFW functions will not work before doing this.
                if (! GLFW.glfwInit())
                {
                    throw Window3DCantBeCreatedException("Initialization of GLFW failed")
                }

                // Configure GLFW
                GLFW.glfwDefaultWindowHints()
                // the window not show for the moment, need some initialization to do before
                GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)

                GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, if (decorated) GLFW.GLFW_TRUE else GLFW.GLFW_FALSE)
                GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE)
                GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, if (maximized) GLFW.GLFW_TRUE else GLFW.GLFW_FALSE)

                // Create the window
                val window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)

                if (window == MemoryUtil.NULL)
                {
                    throw Window3DCantBeCreatedException("Failed to create the GLFW window")
                }

                window3D.windowId = window
                //Register UI events
                GLFW.glfwSetKeyCallback(window, GLFWKeyCallbackI { windowId, key, scanCode, action, modifiers ->
                    window3D.keyEvent(windowId, key, scanCode, action, modifiers)
                })
                GLFW.glfwSetCursorEnterCallback(window, GLFWCursorEnterCallbackI { windowId, entered ->
                    window3D.mouseEntered(windowId, entered)
                })
                GLFW.glfwSetJoystickCallback(
                    GLFWJoystickCallbackI { joystickID, event -> window3D.joystickConnected(joystickID, event) })
                GLFW.glfwSetMouseButtonCallback(window,
                                                GLFWMouseButtonCallbackI { windowId, button, action, modifiers ->
                                                    window3D.mouseButton(windowId, button, action, modifiers)
                                                })
                GLFW.glfwSetCursorPosCallback(window, GLFWCursorPosCallbackI { windowId, cursorX, cursorY ->
                    window3D.mousePosition(windowId, cursorX, cursorY)
                })
                GLFW.glfwSetWindowCloseCallback(window,
                                                GLFWWindowCloseCallbackI { windowId -> window3D.closeWindow(windowId) })

                // Get the thread stack and push a new frame

                var stack = MemoryStack.stackPush()
                var pWidth = stack.mallocInt(1)
                var pHeight = stack.mallocInt(1)

                // Get the window size passed to GLFW.glfwCreateWindow
                GLFW.glfwGetWindowSize(window, pWidth, pHeight)

                // Get the resolution of the primary monitor
                val videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())

                if (videoMode != null)
                {
                    // Center the window
                    GLFW.glfwSetWindowPos(
                        window,
                        (videoMode.width() - pWidth.get(0)) / 2,
                        (videoMode.height() - pHeight.get(0)) / 2)
                }
                else
                {
                    warning("No video mode !")
                }

                MemoryStack.stackPop()


                // Make the window visible
                GLFW.glfwShowWindow(window)

                // Get the thread stack and compute window size

                stack = MemoryStack.stackPush()
                pWidth = stack.mallocInt(1)
                pHeight = stack.mallocInt(1)

                // Get the real window size
                GLFW.glfwGetWindowSize(window, pWidth, pHeight)
                width = pWidth.get()
                height = pHeight.get()
                MemoryStack.stackPop()

                window3D.width = width
                window3D.height = height

                window3D.render3D()
            }

            window3D.readyLocker.lock()
            return window3D
        }
    }

    var width : Int = 0
        private set
    var height : Int = 0
        private set
    private var windowId : Long = 0
    private val waitCloseLocker = Locker()
    private val readyLocker = Locker()
    val preferences : Preferences = Preferences("game/preferences.pref")
    val resources : Resources = Resources(ExternalSource("game/resources"))

    fun close()
    {
        this.closeWindow(this.windowId)
    }

    internal fun waitWindowClose()
    {
        this.waitCloseLocker.lock()
    }

    private fun closeWindow(window : Long)
    {
        //Closing
        GLFW.glfwSetWindowShouldClose(this.windowId, true)

        // TODO

        this.waitCloseLocker.unlock()
    }

    private fun keyEvent(window : Long, key : Int, scanCode : Int, action : Int, modifiers : Int)
    {
        // TODO
    }

    private fun mouseEntered(window : Long, entered : Boolean)
    {
        // TODO
    }

    private fun mouseButton(window : Long, button : Int, action : Int, modifiers : Int)
    {
        // TODO
    }

    private fun mousePosition(window : Long, cursorX : Double, cursorY : Double)
    {
        // TODO
    }

    private fun joystickConnected(joystickID : Int, event : Int)
    {
        // TODO
    }

    private fun initialize3D()
    {
        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(this.windowId)
        // Enable v-sync
        GLFW.glfwSwapInterval(1)

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities()

        // TODO : Lights
        /*
        TEMPORARY_INT_BUFFER.rewind()
        GL11.glGetIntegerv(GL11.GL_MAX_LIGHTS, TEMPORARY_INT_BUFFER)
        TEMPORARY_INT_BUFFER.rewind()
        this.lights = Lights(TEMPORARY_INT_BUFFER.get())
         */

        // *************************
        // *** Initialize OpenGL ***
        // *************************
        // Alpha enable
        GL11.glEnable(GL11.GL_ALPHA_TEST)
        // Set alpha precision
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.01f)
        // Material can be colored
        GL11.glEnable(GL11.GL_COLOR_MATERIAL)
        // For performance disable texture, we enable them only on need
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        // Way to compute alpha
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        // We accept blinding
        GL11.glEnable(GL11.GL_BLEND)
        // Fix the view port
        GL11.glViewport(0, 0, this.width, this.height)
        // Normalization is enable
        GL11.glEnable(GL11.GL_NORMALIZE)
        // Fix the view port. Yes again, I don't know why, but it work better on
        // doing that
        GL11.glViewport(0, 0, this.width, this.height)

        // Set the "3D feeling".
        // That is to say how the 3D looks like
        // Here we want just see the depth, but not have fish eye effect
        GL11.glMatrixMode(GL11.GL_PROJECTION)
        GL11.glLoadIdentity()
        val ratio = this.width.toFloat() / this.height.toFloat()
        gluPerspective(45.0, ratio.toDouble(), 0.1, 5000.0)
        GL11.glMatrixMode(GL11.GL_MODELVIEW)
        GL11.glLoadIdentity()

        // Initialize background
        GL11.glClearColor(1f, 1f, 1f, 1f)
        GL11.glEnable(GL11.GL_DEPTH_TEST)

        // Enable see and hide face
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glCullFace(GL11.GL_FRONT)

        // Light base adjustment for smooth effect
        GL11.glLightModeli(GL11.GL_LIGHT_MODEL_LOCAL_VIEWER, GL11.GL_TRUE)
        GL11.glShadeModel(GL11.GL_SMOOTH)
        GL11.glLightModeli(GL12.GL_LIGHT_MODEL_COLOR_CONTROL, GL12.GL_SEPARATE_SPECULAR_COLOR)
        GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, 1)

        // Enable lights and default light
        GL11.glEnable(GL11.GL_LIGHTING)
    }

    private fun renderScene()
    {
        try
        {
            // TODO
            // Draw the background and clear Z-Buffer
            // this.currentScene.drawBackground()
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

            // Draw 2D objects under 3D
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            // this.drawUnder3D()
            GL11.glEnable(GL11.GL_DEPTH_TEST)

            // Render the scene
            GL11.glPushMatrix()
            //  this.currentScene.renderTheScene(this)
            GL11.glPopMatrix()

            // Draw 2D objects over 3D
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            //  this.drawOver3D()
        }
        catch (ignored : Exception)
        {
        }
        catch (ignored : Error)
        {
        }
    }

    private fun renderLoop()
    {
        // TODO
        this.renderScene()
    }

    private fun render3D()
    {
        this.initialize3D()

        this.readyLocker.unlock()

        while (! GLFW.glfwWindowShouldClose(this.windowId))
        {
            // clear the framebuffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

            this.renderLoop()

            // TODO : COMPLETE

            // swap the color buffers
            GLFW.glfwSwapBuffers(this.windowId)

            // Poll for window events. The key callback will only be invoked during this call.
            GLFW.glfwPollEvents()
        }

        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(this.windowId)
        GLFW.glfwDestroyWindow(this.windowId)

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)
            ?.free()
    }
}
