package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUIMargin
import khelp.ui.events.MouseState
import khelp.ui.style.COMPONENT_HIGHEST_LEVEL
import khelp.ui.style.ComponentHighLevel
import khelp.ui.style.background.StyleBackground
import khelp.ui.style.background.StyleBackgroundTransparent
import khelp.ui.style.shape.StyleShape
import khelp.ui.style.shape.StyleShapeRectangle
import khelp.ui.utilities.SHADOW
import khelp.ui.utilities.TRANSPARENT
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.geom.Area

abstract class GUIComponent
{
    internal var x = 0
    internal var y = 0
    internal var width = 16
    internal var height = 16
    internal var refresh : () -> Unit = {}
    var margin : GUIMargin = GUIMargin()
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }
    var visible : Boolean = true
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }

    var borderColor : Color = TRANSPARENT
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }

    var shape : StyleShape = StyleShapeRectangle
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }

    var background : StyleBackground = StyleBackgroundTransparent
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }

    var componentHighLevel : ComponentHighLevel = ComponentHighLevel.AT_GROUND
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }

    fun contains(x : Int, y : Int) : Boolean =
        x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height

    internal open fun mouseState(mouseState : MouseState) : Boolean = false

    fun draw(graphics2D : Graphics2D)
    {
        val highLevel = this.componentHighLevel.level
        val x = COMPONENT_HIGHEST_LEVEL - highLevel
        val y = COMPONENT_HIGHEST_LEVEL - highLevel
        val width = this.width - highLevel - x
        val height = this.height - highLevel - y
        val shape = this.shape.createShape(x, y, width - 2, height - 2)

        if (highLevel > 0)
        {
            val shadowShape = this.shape.createShape(x + highLevel, y + highLevel, width, height)
            val area = Area(shadowShape)
            area.subtract(Area(shape))
            graphics2D.color = SHADOW
            graphics2D.fill(area)
        }

        val additional = this.shape.margin(x, y, width - 2, height - 2)
        val margin = GUIMargin(this.margin.left + additional.left + x, this.margin.right + additional.right - x,
                               this.margin.top + additional.top + y, this.margin.bottom + additional.bottom - y)
        this.background.applyOnShape(graphics2D, shape)

        if (this.borderColor.alpha > 0)
        {
            graphics2D.color = this.borderColor
            val stroke = graphics2D.stroke
            graphics2D.stroke = BasicStroke(3f)
            graphics2D.draw(shape)
            graphics2D.stroke = stroke
        }

        this.drawIntern(graphics2D, margin)
    }

    protected abstract fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)

    fun preferredSize() : Dimension
    {
        val highLevel = this.componentHighLevel.level
        val x = COMPONENT_HIGHEST_LEVEL - highLevel
        val y = COMPONENT_HIGHEST_LEVEL - highLevel
        val width = this.width - highLevel - x
        val height = this.height - highLevel - y
        val additional = this.shape.margin(x, y, width - 2, height - 2)
        val margin = GUIMargin(this.margin.left + additional.left, this.margin.right + additional.right,
                               this.margin.top + additional.top, this.margin.bottom + additional.bottom)
        return this.preferredSize(margin)
    }

    protected abstract fun preferredSize(margin : GUIMargin) : Dimension
}
