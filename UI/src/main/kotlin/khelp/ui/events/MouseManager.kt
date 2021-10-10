package khelp.ui.events

import khelp.thread.delay
import khelp.thread.future.FutureResult
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import java.awt.Component
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import javax.swing.SwingUtilities

class MouseManager private constructor() : MouseListener,
                                           MouseMotionListener
{
    companion object
    {
        fun attachTo(component : Component) : MouseManager
        {
            val mouseManager = MouseManager()
            component.addMouseListener(mouseManager)
            component.addMouseMotionListener(mouseManager)
            return mouseManager
        }
    }

    var mouseStatus = MouseStatus.OUTSIDE
        private set
    var x = Int.MIN_VALUE
        private set
    var y = Int.MIN_VALUE
        private set
    var leftButtonDown = false
        private set
    var middleButtonDown = false
        private set
    var rightButtonDown = false
        private set
    var control : Boolean = false
        internal set
    var shift : Boolean = false
        private set
    var alt : Boolean = false
        private set

    private var startClickTime = 0L
    private var timeOut : FutureResult<Unit>? = null
    private val mouseStateObservableData =
        ObservableData<MouseState>(
            MouseState(MouseStatus.OUTSIDE, Int.MIN_VALUE, Int.MIN_VALUE,
                       leftButtonDown = false, middleButtonDown = false, rightButtonDown = false,
                       shiftDown = false, controlDown = false, altDown = false,
                       clicked = false))
    val mouseStateObservable : Observable<MouseState> = this.mouseStateObservableData.observable

    override fun mouseClicked(mouseEvent : MouseEvent)
    {
        this.mouseStateObservableData.value(
            MouseState(this.mouseStatus, this.x, this.y,
                       this.leftButtonDown, this.middleButtonDown, this.rightButtonDown,
                       this.shift, this.control, this.alt,
                       clicked = true))
    }

    private fun state(mouseStatus : MouseStatus, mousePress : MousePress, mouseEvent : MouseEvent)
    {
        this.timeOut?.cancel("State changed")
        this.timeOut = null

        this.mouseStatus = mouseStatus
        this.x = mouseEvent.x
        this.y = mouseEvent.y
        this.leftButtonDown = mousePress.press(this.leftButtonDown, SwingUtilities.isLeftMouseButton(mouseEvent))
        this.middleButtonDown = mousePress.press(this.middleButtonDown, SwingUtilities.isMiddleMouseButton(mouseEvent))
        this.rightButtonDown = mousePress.press(this.rightButtonDown, SwingUtilities.isRightMouseButton(mouseEvent))
        this.shift = (mouseEvent.modifiersEx and MouseEvent.SHIFT_DOWN_MASK) != 0
        this.control = (mouseEvent.modifiersEx and MouseEvent.CTRL_DOWN_MASK) != 0
        this.alt = (mouseEvent.modifiersEx and MouseEvent.ALT_DOWN_MASK) != 0

        val mouseState = MouseState(this.mouseStatus, this.x, this.y,
                                    this.leftButtonDown, this.middleButtonDown, this.rightButtonDown,
                                    this.shift, this.control, this.alt,
                                    clicked = false)

        if (this.mouseStateObservableData.value() != mouseState)
        {
            this.mouseStateObservableData.value(mouseState)
        }

        if (mouseStatus == MouseStatus.MOVE || mouseStatus == MouseStatus.DRAG)
        {
            this.timeOut = delay(128) {
                val mouseStateStay =
                    MouseState(MouseStatus.STAY, this.x, this.y,
                               this.leftButtonDown, this.middleButtonDown, this.rightButtonDown,
                               this.shift, this.control, this.alt,
                               clicked = false)

                if (this.mouseStateObservableData.value() != mouseStateStay)
                {
                    this.mouseStateObservableData.value(mouseStateStay)
                }
            }
        }
    }

    override fun mousePressed(mouseEvent : MouseEvent)
    {
        this.state(MouseStatus.STAY, MousePress.PRESS, mouseEvent)
    }

    override fun mouseReleased(mouseEvent : MouseEvent)
    {
        this.state(MouseStatus.STAY, MousePress.RELEASE, mouseEvent)
    }

    override fun mouseEntered(mouseEvent : MouseEvent)
    {
        this.state(MouseStatus.MOVE, MousePress.UNDEFINED, mouseEvent)
    }

    override fun mouseExited(mouseEvent : MouseEvent)
    {
        this.state(MouseStatus.OUTSIDE, MousePress.UNDEFINED, mouseEvent)
    }

    override fun mouseDragged(mouseEvent : MouseEvent)
    {
        this.state(MouseStatus.DRAG, MousePress.PRESS, mouseEvent)
    }

    override fun mouseMoved(mouseEvent : MouseEvent)
    {
        this.state(MouseStatus.MOVE, MousePress.RELEASE, mouseEvent)
    }
}
