package khelp.match3.ui

import khelp.thread.flow.Flow
import khelp.thread.flow.FlowData
import khelp.utilities.log.debug
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

class MouseManager(val screenWidth : Int, val screenHeight : Int) : MouseListener,
                                                                    MouseMotionListener
{
    private val eventMouseFlowData = FlowData<EventMouse>()
    val eventMouseFlow : Flow<EventMouse> = this.eventMouseFlowData.flow

    override fun mouseClicked(mouseEvent : MouseEvent)
    {
        this.publish(mouseEvent.x, mouseEvent.y, EventMouseType.CLiCK)
    }

    override fun mousePressed(mouseEvent : MouseEvent) = Unit

    override fun mouseReleased(mouseEvent : MouseEvent) = Unit

    override fun mouseEntered(mouseEvent : MouseEvent) = Unit

    override fun mouseExited(mouseEvent : MouseEvent) = Unit

    override fun mouseDragged(mouseEvent : MouseEvent)
    {
        this.publish(mouseEvent.x, mouseEvent.y, EventMouseType.DRAG)
    }

    override fun mouseMoved(mouseEvent : MouseEvent)
    {
        this.publish(mouseEvent.x, mouseEvent.y, EventMouseType.MOVE)
    }

    private fun publish(x : Int, y : Int, type : EventMouseType)
    {
        this.eventMouseFlowData.publish(EventMouse((x * 1024) / this.screenWidth, (y * 1024) / this.screenHeight, type))
    }
}
