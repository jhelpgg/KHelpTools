package khelp.engine3d.render

import khelp.engine3d.event.ActionManager
import khelp.engine3d.event.MouseManager3D
import khelp.engine3d.gui.GUI
import khelp.engine3d.sound3d.SoundManager
import khelp.engine3d.utils.TEMPORARY_FLOAT_BUFFER
import khelp.engine3d.utils.ThreadOpenGL
import khelp.engine3d.utils.gluPerspective
import khelp.io.ExternalSource
import khelp.preferences.Preferences
import khelp.resources.Resources
import khelp.thread.Locker
import khelp.thread.flow.Flow
import khelp.thread.flow.FlowData
import khelp.thread.parallel
import khelp.ui.events.MouseStatus
import khelp.utilities.log.verbose
import khelp.utilities.log.warning
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import kotlin.math.max


@WindowDSL
fun window3D(width : Int, height : Int, title : String, windowCreator : Window3D.() -> Unit)
{
    val window3D = Window3D.createWindow3D(- 1, - 1, width, height, title, true, false, false)
    windowCreator(window3D)
    window3D.waitWindowClose()
}

@WindowDSL
fun window3DFull(title : String, windowCreator : Window3D.() -> Unit)
{
    val window3D = Window3D.createWindow3D(- 1, - 1, 800, 600, title, false, true, false)
    windowCreator(window3D)
    window3D.waitWindowClose()
}

@WindowDSL
fun window3DFix(x : Int, y : Int, width : Int, height : Int, title : String, windowCreator : Window3D.() -> Unit)
{
    val window3D = Window3D.createWindow3D(max(0, x), max(0, y), width, height, title, false, false, true)
    windowCreator(window3D)
    window3D.waitWindowClose()
}

class Window3D private constructor()
{
    companion object
    {
        internal fun createWindow3D(x : Int, y : Int,
                                    requestWidth : Int, requestHeight : Int, title : String,
                                    decorated : Boolean, maximized : Boolean, atTop : Boolean) : Window3D
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
                GLFW.glfwWindowHint(GLFW.GLFW_FLOATING, if (atTop) GLFW.GLFW_TRUE else GLFW.GLFW_FALSE)

                // Create the window
                val window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)

                if (window == MemoryUtil.NULL)
                {
                    throw Window3DCantBeCreatedException("Failed to create the GLFW window")
                }

                window3D.windowId = window
                //Register UI events
                GLFW.glfwSetKeyCallback(window) { _, key, _, action, _ ->
                    window3D.keyEvent(key, action)
                }
                GLFW.glfwSetCursorEnterCallback(window) { _, entered ->
                    window3D.mouseEntered(entered)
                }
                GLFW.glfwSetJoystickCallback { joystickID, event -> window3D.joystickConnected(joystickID, event) }
                GLFW.glfwSetMouseButtonCallback(window) { _, button, action, modifiers ->
                    window3D.mouseButton(button, action, modifiers)
                }
                GLFW.glfwSetCursorPosCallback(window) { _, cursorX, cursorY ->
                    window3D.mousePosition(cursorX, cursorY)
                }
                GLFW.glfwSetWindowCloseCallback(window) { window3D.closeWindow() }

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
                        if (x >= 0) x + (width - pWidth.get(0)) / 2 else (videoMode.width() - pWidth.get(0)) / 2,
                        if (y >= 0) y + (height - pHeight.get(0)) / 2 else (videoMode.height() - pHeight.get(0)) / 2)
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
    var canCloseNow : () -> Boolean = { true }
    val scene = Scene()
    private var windowId : Long = 0
    private val waitCloseLocker = Locker()
    private val readyLocker = Locker()
    val preferences : Preferences = Preferences("game/preferences.pref")
    val resources : Resources = Resources(ExternalSource("game/resources"))
    val actionManager = ActionManager(this.preferences)
    lateinit var mouseManager : MouseManager3D
        private set
    private var ready = false
    private var nodeDetect : Node? = null
    private val nodePickedFlowData = FlowData<Node?>()
    val nodePickedFlow : Flow<Node?> = this.nodePickedFlowData.flow
    lateinit var gui : GUI
        private set

    /**
     * Sound manager
     */
    val soundManager : SoundManager by lazy { SoundManager() }


    fun <N : Node> findById(id : String) : N? = this.scene.findById(id)

    fun close()
    {
        this.closeWindow()
    }

    internal fun waitWindowClose()
    {
        this.waitCloseLocker.lock()
    }

    private fun closeWindow()
    {
        if (! this.canCloseNow())
        {
            //Avoid the closing
            GLFW.glfwSetWindowShouldClose(this.windowId, false)
            return
        }

        //Closing
        GLFW.glfwSetWindowShouldClose(this.windowId, true)
        this.waitCloseLocker.unlock()
    }

    private fun keyEvent(key : Int, action : Int)
    {
        this.actionManager.keyEvent(key, action)
    }

    private fun mouseEntered(entered : Boolean)
    {
        if (this.ready)
        {
            this.mouseManager.mouseEntered(entered)
        }
    }

    private fun mouseButton(button : Int, action : Int, modifiers : Int)
    {
        if (this.ready)
        {
            this.mouseManager.mouseButton(button, action, modifiers)
        }
    }

    private fun mousePosition(cursorX : Double, cursorY : Double)
    {
        if (this.ready)
        {
            this.mouseManager.mousePosition(cursorX, cursorY)
        }
    }

    private fun joystickConnected(joystickID : Int, event : Int)
    {
        //GLFW_CONNECTED
        //GLFW_DISCONNECTED
        // TODO
    }

    @ThreadOpenGL
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

    @ThreadOpenGL
    private fun renderScene()
    {
        try
        {
            // TODO
            // Draw the background and clear Z-Buffer
            this.scene.drawBackground()
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

            // Draw 2D objects under 3D
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            // this.drawUnder3D()
            GL11.glEnable(GL11.GL_DEPTH_TEST)

            // Render the scene
            GL11.glPushMatrix()
            this.scene.render()
            GL11.glPopMatrix()

            // Draw 2D objects over 3D
            this.gui.update()
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glPushMatrix()
            this.gui.plane.matrixRootToMe()
            this.gui.plane.renderSpecific()
            GL11.glPopMatrix()
        }
        catch (ignored : Exception)
        {
        }
        catch (ignored : Error)
        {
        }
    }

    /**
     * Render the scene on picking mode
     */
    @ThreadOpenGL
    private fun renderPicking()
    {
        // Prepare for "picking rendering"
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glDisable(GL11.GL_CULL_FACE)
        GL11.glClearColor(1f, 1f, 1f, 1f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        GL11.glPushMatrix()

        // Render the scene in picking mode
        this.scene.root.renderTheNodePicking()
        GL11.glPopMatrix()
        GL11.glEnable(GL11.GL_LIGHTING)

        val previousNode = this.nodeDetect

        // If detection point is on the screen
        if (this.mouseManager.mouseStatus != MouseStatus.OUTSIDE
            && this.mouseManager.mouseX >= 0 && this.mouseManager.mouseX < this.width
            && this.mouseManager.mouseY >= 0 && this.mouseManager.mouseY < this.height)
        {
            // Compute pick color and node pick
            this.nodeDetect = this.scene.root
                .pickingNode(this.pickColor(this.mouseManager.mouseX, this.mouseManager.mouseY))
        }
        else
        {
            this.nodeDetect = null
        }

        val actualNode = this.nodeDetect

        if (previousNode != actualNode)
        {
            if (previousNode is NodeWithMaterial)
            {
                previousNode.selected = false
            }

            if (actualNode is NodeWithMaterial)
            {
                actualNode.selected = true
            }

            this.nodePickedFlowData.publish(actualNode)
        }
    }

    @ThreadOpenGL
    private fun renderLoop()
    {
        this.renderPicking()
        this.renderScene()
    }

    @ThreadOpenGL
    private fun render3D()
    {
        this.actionManager.computeAxisLimits()
        this.soundManager.init()
        this.gui = GUI(this.width, this.height)
        this.mouseManager = MouseManager3D(this.width, this.height, this.gui)
        this.ready = true
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
            this.actionManager.publishActions()
        }

        this.soundManager.destroy()
        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(this.windowId)
        GLFW.glfwDestroyWindow(this.windowId)

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)
            ?.free()
        verbose("Good bye !")
    }

    @ThreadOpenGL
    private fun pickColor(x : Int, y : Int) : Color4f
    {
        // Get picking color
        TEMPORARY_FLOAT_BUFFER.rewind()
        GL11.glReadPixels(x, this.height - y, 1, 1, GL11.GL_RGBA, GL11.GL_FLOAT, TEMPORARY_FLOAT_BUFFER)
        TEMPORARY_FLOAT_BUFFER.rewind()

        // Convert in RGB value
        val red = TEMPORARY_FLOAT_BUFFER.get()
        val green = TEMPORARY_FLOAT_BUFFER.get()
        val blue = TEMPORARY_FLOAT_BUFFER.get()
        TEMPORARY_FLOAT_BUFFER.rewind()

        return Color4f(red, green, blue)
    }
}
