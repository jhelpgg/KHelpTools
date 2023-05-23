package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUIMargin
import khelp.engine3d.gui.layout.GUIConstraints
import khelp.engine3d.gui.layout.GUILayout
import khelp.ui.events.MouseState
import java.awt.Dimension
import java.awt.Graphics2D

class GUIComponentPanel<C : GUIConstraints, L : GUILayout<C>>(private val layout : L) : GUIComponent()
{
    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        val transform = graphics2D.transform
        val clip = graphics2D.clip
        val parentWidth = this.width - margin.width
        val parentHeight = this.height - margin.height
        graphics2D.clipRect(margin.left, margin.top, parentWidth, parentHeight)
        graphics2D.translate(margin.left, margin.top)
        this.layout.layout(parentWidth, parentHeight)

        for (component in this.layout.components())
        {
            if (component.visible)
            {
                val clip2 = graphics2D.clip
                val transform2 = graphics2D.transform
                graphics2D.clipRect(component.x, component.y, component.width, component.height)
                graphics2D.translate(component.x, component.y)
                component.draw(graphics2D)
                graphics2D.transform = transform2
                graphics2D.clip = clip2
            }
        }

        graphics2D.transform = transform
        graphics2D.clip = clip
    }

    override fun preferredSize(margin : GUIMargin) : Dimension
    {
        val preferred = this.layout.preferredSize()
        return Dimension(preferred.width + margin.width, preferred.height + margin.height)
    }

    override fun mouseState(mouseState : MouseState) : Boolean
    {
        val x = mouseState.x
        val y = mouseState.y

        for (component in this.layout.components())
        {
            if (component.visible && component.contains(x, y))
            {
                val state = MouseState(mouseState.mouseStatus,
                                       mouseState.x - component.x, mouseState.y - component.y,
                                       mouseState.leftButtonDown, mouseState.middleButtonDown,
                                       mouseState.rightButtonDown,
                                       mouseState.shiftDown, mouseState.controlDown, mouseState.altDown,
                                       mouseState.clicked)
                return component.mouseState(state)
            }
        }

        return false
    }
}
