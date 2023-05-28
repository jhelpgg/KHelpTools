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
    var margin : GUIMargin = GUIMargin()
    var visible : Boolean = true
    var borderColor : Color = TRANSPARENT
    var shape : StyleShape = StyleShapeRectangle
    var background : StyleBackground = StyleBackgroundTransparent
    var componentHighLevel : ComponentHighLevel = ComponentHighLevel.AT_GROUND

    var marginLeft : Int
        get() = this.margin.left
        set(value)
        {
            this.margin = this.margin.left(value)
        }

    var marginRight : Int
        get() = this.margin.right
        set(value)
        {
            this.margin = this.margin.right(value)
        }

    var marginTop : Int
        get() = this.margin.top
        set(value)
        {
            this.margin = this.margin.top(value)
        }

    var marginBottom : Int
        get() = this.margin.bottom
        set(value)
        {
            this.margin = this.margin.bottom(value)
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
        val margin = GUIMargin(this.margin.left + additional.left, this.margin.right + additional.right,
                               this.margin.top + additional.top, this.margin.bottom + additional.bottom)
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
        val margin = GUIMargin(this.margin.left, this.margin.right,
                               this.margin.top, this.margin.bottom)
        val preferred = this.preferredSize(margin)
        return Dimension(preferred.width + x + additional.left + additional.right,
                         preferred.height + y + additional.top + additional.bottom)
    }

    protected abstract fun preferredSize(margin : GUIMargin) : Dimension
}
