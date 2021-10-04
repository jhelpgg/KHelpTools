package khelp.engine3d.event

import khelp.engine3d.utils.ThreadOpenGL
import khelp.preferences.Preferences
import khelp.thread.Mutex
import khelp.thread.extensions.futureFail
import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.utilities.collections.ImmutableList
import org.lwjgl.glfw.GLFW
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.min

class ActionManager internal constructor(private val preferences : Preferences)
{
    companion object
    {
        private const val JOYSTICK_CODE = ".joystickCode"
        private const val KEY_CODE = ".keyCode"
    }

    private val currentActionCodes = ArrayList<ActionCode>()
    private val actionDescriptions = ArrayList<ActionDescription>()

    /**Current active key codes*/
    private val activeKeys = HashSet<Int>()

    private val axisLimits = Array<AxeLimits>(JoystickCode.MAX_AXIS_INDEX + 1) { AxeLimits(0f) }

    /**Current active joystick codes and their status*/
    private val currentJoystickCodes = HashMap<JoystickCode, JoystickStatus>()

    /**Copy of last current active joystick codes and their status*/
    private val currentJoystickCodesCopy = HashMap<JoystickCode, JoystickStatus>()

    private val mutexCapture = Mutex()
    private var nextKeyCode : Promise<Int>? = null
    private var nextJoystickCode : Promise<JoystickCode>? = null
    private val canCaptureJoystick = AtomicBoolean(false)

    private val actionObservableData = ObservableData<List<ActionCode>>(emptyList())
    val actionObservable : Observable<List<ActionCode>> = this.actionObservableData.observable

    init
    {
        for (joystickCode in JoystickCode.values())
        {
            this.currentJoystickCodes[joystickCode] = JoystickStatus.RELEASED
        }

        for (actionCode in ActionCode.values())
        {
            val joystickCode =
                this.preferences[actionCode.preferenceKey + JOYSTICK_CODE, actionCode.defaultJoystickCode]
            val keyCode = this.preferences[actionCode.preferenceKey + KEY_CODE, actionCode.defaultKeyCode]
            this.actionDescriptions.add(ActionDescription(actionCode, joystickCode, keyCode))
        }

        this.actionDescriptions.trimToSize()
    }

    fun associate(actionCode : ActionCode, joystickCode : JoystickCode) : JoystickCode
    {
        for (actionDescription in this.actionDescriptions)
        {
            if (actionDescription.actionCode == actionCode)
            {
                val oldJoystickCode = actionDescription.joystickCode

                if (oldJoystickCode != joystickCode)
                {
                    actionDescription.joystickCode = joystickCode
                    this.preferences.edit { actionCode.preferenceKey + JOYSTICK_CODE IS joystickCode }
                }

                return oldJoystickCode
            }
        }

        return JoystickCode.NONE
    }

    fun associate(actionCode : ActionCode, keyCode : Int) : Int
    {
        for (actionDescription in this.actionDescriptions)
        {
            if (actionDescription.actionCode == actionCode)
            {
                val oldKeyCode = actionDescription.keyCode

                if (oldKeyCode != keyCode)
                {
                    actionDescription.keyCode = keyCode
                    this.preferences.edit { actionCode.preferenceKey + KEY_CODE IS keyCode }
                }

                return oldKeyCode
            }
        }

        return GLFW.GLFW_KEY_UNKNOWN
    }

    fun joystickAssociation(actionCode : ActionCode) : JoystickCode
    {
        for (actionDescription in this.actionDescriptions)
        {
            if (actionDescription.actionCode == actionCode)
            {
                return actionDescription.joystickCode
            }
        }

        return JoystickCode.NONE
    }

    fun keyAssociation(actionCode : ActionCode) : Int
    {
        for (actionDescription in this.actionDescriptions)
        {
            if (actionDescription.actionCode == actionCode)
            {
                return actionDescription.keyCode
            }
        }

        return GLFW.GLFW_KEY_UNKNOWN
    }

    fun consumable(actionCode : ActionCode) : Boolean
    {
        for (actionDescription in this.actionDescriptions)
        {
            if (actionDescription.actionCode == actionCode)
            {
                return actionDescription.consumable
            }
        }

        return false
    }

    fun consumable(actionCode : ActionCode, consumable : Boolean)
    {
        for (actionDescription in this.actionDescriptions)
        {
            if (actionDescription.actionCode == actionCode)
            {
                actionDescription.consumable = consumable
                return
            }
        }
    }

    /**
     * Capture the next joystick event
     *
     * @return Future that will contains the next joystick event
     */
    fun captureJoystick() : FutureResult<JoystickCode> =
        this.mutexCapture {
            if (this.nextJoystickCode == null)
            {
                this.nextJoystickCode = Promise<JoystickCode>()
                this.canCaptureJoystick.set(false)

                for ((key, value) in this.currentJoystickCodes)
                {
                    this.currentJoystickCodesCopy[key] = value
                }
            }

            this.nextJoystickCode?.futureResult ?: Exception("Fail to capture joystick code").futureFail()
        }

    /**
     * Capture the next key typed
     *
     * @return Future that will contains the next key code typed
     */
    fun captureKeyCode() : FutureResult<Int> =
        this.mutexCapture {
            if (this.nextKeyCode == null)
            {
                this.nextKeyCode = Promise<Int>()
            }

            this.nextKeyCode?.futureResult ?: Exception("Fail to capture key code").futureFail()
        }

    /**
     * Called when key event happen
     *
     * @param keyCode Key code
     * @param action  Key action: [GLFW.GLFW_PRESS], [GLFW.GLFW_REPEAT] or [GLFW.GLFW_RELEASE]
     */
    @ThreadOpenGL
    internal fun keyEvent(keyCode : Int, action : Int)
    {
        if (action == GLFW.GLFW_PRESS)
        {
            val consumed = this.mutexCapture {
                if (this.nextKeyCode != null)
                {
                    this.nextKeyCode?.result(keyCode)
                    this.nextKeyCode = null
                    this.activeKeys.remove(keyCode)
                    true
                }
                else
                {
                    false
                }
            }

            if (consumed)
            {
                return
            }

            this.activeKeys.add(keyCode)
        }
        else if (action == GLFW.GLFW_RELEASE)
        {
            this.activeKeys.remove(keyCode)
        }
    }

    internal fun computeAxisLimits()
    {
        if (GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1))
        {
            val axes = GLFW.glfwGetJoystickAxes(GLFW.GLFW_JOYSTICK_1)

            if (axes != null)
            {
                val position = axes.position()
                val axesValue = FloatArray(axes.limit() - position)
                axes.get(axesValue)
                axes.position(position)
                val max = min(axesValue.size - 1, JoystickCode.MAX_AXIS_INDEX)

                for (index in 0 .. max)
                {
                    this.axisLimits[index] = AxeLimits(axesValue[index])
                }
            }
        }
    }

    /**
     * Post action to listeners
     */
    @ThreadOpenGL
    internal fun publishActions()
    {
        //Collect joystick status
        if (GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1))
        {
            val axes = GLFW.glfwGetJoystickAxes(GLFW.GLFW_JOYSTICK_1)

            if (axes != null)
            {
                val position = axes.position()
                val axesValue = FloatArray(axes.limit() - position)
                axes.get(axesValue)
                axes.position(position)
                val max = min(axesValue.size - 1, JoystickCode.MAX_AXIS_INDEX)

                for (index in 0 .. max)
                {
                    val axeWay = this.axisLimits[index].way(axesValue[index])

                    if (axeWay == AxeWay.NEGATIVE)
                    {
                        this.currentJoystickCodes[JoystickCode.obtainAxis(index, true)] = JoystickStatus.RELEASED

                        if (this.pressJoystick(JoystickCode.obtainAxis(index, false)))
                        {
                            return
                        }
                    }
                    else if (axeWay == AxeWay.POSITIVE)
                    {
                        this.currentJoystickCodes[JoystickCode.obtainAxis(index, false)] = JoystickStatus.RELEASED

                        if (this.pressJoystick(JoystickCode.obtainAxis(index, true)))
                        {
                            return
                        }
                    }
                    else
                    {
                        this.currentJoystickCodes[JoystickCode.obtainAxis(index, true)] = JoystickStatus.RELEASED
                        this.currentJoystickCodes[JoystickCode.obtainAxis(index, false)] = JoystickStatus.RELEASED
                    }
                }
            }

            val buttons = GLFW.glfwGetJoystickButtons(GLFW.GLFW_JOYSTICK_1)

            if (buttons != null)
            {
                val position = buttons.position()
                val buttonsStatus = ByteArray(buttons.limit() - position)
                buttons.get(buttonsStatus)
                buttons.position(position)
                val max = min(buttonsStatus.size - 1, JoystickCode.MAX_BUTTON_INDEX)

                for (index in 0 .. max)
                {
                    if (buttonsStatus[index].toInt() == GLFW.GLFW_PRESS)
                    {
                        if (this.pressJoystick(JoystickCode.obtainButton(index)))
                        {
                            return
                        }
                    }
                    else
                    {
                        this.currentJoystickCodes[JoystickCode.obtainButton(index)] = JoystickStatus.RELEASED
                    }
                }
            }
        }

        //Update actions active list
        this.actionDescriptions.forEach { actionDescription ->
            val joystickStatus = this.currentJoystickCodes[actionDescription.joystickCode]

            if (this.activeKeys.contains(actionDescription.keyCode) ||
                joystickStatus === JoystickStatus.PRESSED ||
                (joystickStatus === JoystickStatus.REPEATED && ! actionDescription.consumable))
            {
                if (! this.currentActionCodes.contains(actionDescription.actionCode))
                {
                    this.currentActionCodes.add(actionDescription.actionCode)
                }
            }
            else
            {
                this.currentActionCodes.remove(actionDescription.actionCode)
            }
        }

        this.actionObservableData.value(ImmutableList(this.currentActionCodes))

        //Publish active actions
        if (this.currentActionCodes.isNotEmpty())
        {
            this.actionDescriptions.forEach { actionDescription ->
                if (actionDescription.consumable)
                {
                    this.currentActionCodes.remove(actionDescription.actionCode)
                    this.activeKeys.remove(actionDescription.keyCode)
                }
            }
        }
    }

    /**
     * Press a joystick input
     *
     * @param joystickCode Joystick code
     * @return Indicates if event is consumed
     */
    private fun pressJoystick(joystickCode : JoystickCode) : Boolean
    {
        val consumed = this.mutexCapture {
            if (this.nextJoystickCode != null)
            {
                if (this.canCaptureJoystick.compareAndSet(true, false))
                {
                    this.nextJoystickCode?.result(joystickCode)
                    this.nextJoystickCode = null

                    for ((key, value) in this.currentJoystickCodesCopy)
                    {
                        this.currentJoystickCodes[key] = value
                    }
                }
                else
                {
                    this.canCaptureJoystick.set(this.currentJoystickCodes.values.all { it === JoystickStatus.RELEASED })
                }

                true
            }
            else
            {
                false
            }
        }

        if (consumed)
        {
            return true
        }

        this.currentJoystickCodes[joystickCode] = this.currentJoystickCodes[joystickCode] !!.press()
        return false
    }
}
