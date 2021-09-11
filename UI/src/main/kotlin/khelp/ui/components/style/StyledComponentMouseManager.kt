package khelp.ui.components.style

import khelp.ui.style.Style
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.lang.ref.WeakReference

internal class StyledComponentMouseManager : MouseListener
{
    var mouseEvent : (MouseEvent) -> Unit = {}

    override fun mouseClicked(mouseEvent : MouseEvent)
    {
        this.mouseEvent(mouseEvent)
    }

    override fun mousePressed(mouseEvent : MouseEvent)
    {
        this.mouseEvent(mouseEvent)
    }

    override fun mouseReleased(mouseEvent : MouseEvent)
    {
        this.mouseEvent(mouseEvent)
    }

    override fun mouseEntered(mouseEvent : MouseEvent)
    {
        this.mouseEvent(mouseEvent)
    }

    override fun mouseExited(mouseEvent : MouseEvent)
    {
        this.mouseEvent(mouseEvent)
    }
}
