package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUIMargin
import khelp.thread.parallel
import khelp.ui.events.MouseState
import khelp.ui.events.MouseStatus
import java.awt.Dimension
import java.awt.Graphics2D
import kotlin.math.max

class GUIComponentButton(private val normal : GUIComponent,
                         private val over : GUIComponent,
                         private val down : GUIComponent,
                         private val disabled : GUIComponent) : GUIComponent()
{
    var enabled : Boolean = true
    var click : () -> Unit = {}

    private var isOver = false
    private var isDown = false

    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        val component =
            when
            {
                ! this.enabled -> this.disabled
                this.isDown    -> this.down
                this.isOver    -> this.over
                else           -> this.normal
            }

        component.margin = this.margin
        component.x = 0
        component.y = 0
        component.width = this.width
        component.height = this.height
        component.draw(graphics2D)
    }

    override fun preferredSize(margin : GUIMargin) : Dimension
    {
        var width = 16
        var height = 16

        var preferred = this.normal.preferredSize()
        width = max(width, preferred.width)
        height = max(height, preferred.height)

        preferred = this.over.preferredSize()
        width = max(width, preferred.width)
        height = max(height, preferred.height)

        preferred = this.down.preferredSize()
        width = max(width, preferred.width)
        height = max(height, preferred.height)

        preferred = this.disabled.preferredSize()
        width = max(width, preferred.width)
        height = max(height, preferred.height)

        return Dimension(width + margin.width, height + margin.height)
    }

    override fun mouseState(mouseState : MouseState) : Boolean
    {
        if (this.enabled)
        {
            when
            {
                mouseState.mouseStatus == MouseStatus.ENTER ->
                    if (! this.isOver)
                    {
                        this.isOver = true
                    }

                mouseState.mouseStatus == MouseStatus.EXIT  ->
                    if (this.isOver)
                    {
                        this.isOver = false
                        this.isDown = false
                    }

                mouseState.leftButtonDown                   ->
                    if (! this.isDown)
                    {
                        this.isDown = true
                    }

                ! mouseState.leftButtonDown                 ->
                    if (this.isDown)
                    {
                        this.isDown = false
                        parallel(task = this.click)
                    }
            }

            return true
        }

        return false
    }
}
