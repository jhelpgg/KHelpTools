package khelp.ui.components.style

import khelp.thread.TaskContext
import khelp.thread.delay
import khelp.ui.style.COMPONENT_HIGHEST_LEVEL
import khelp.ui.style.Style
import khelp.ui.utilities.SHADOW
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.geom.Area
import javax.swing.JComponent

abstract class StyledComponent<S : Style>(val style : S) : JComponent()
{
    init
    {
        delay(128)
        {
            this.refresh()
            this.size = this.preferredSize
            this.style.changeStyleObservable.observedBy(TaskContext.INDEPENDENT) { this.refresh() }
        }
    }

    final override fun paintComponent(graphics : Graphics)
    {
        val graphics2D = graphics as Graphics2D
        val highLevel = this.style.componentHighLevel.level
        val x = COMPONENT_HIGHEST_LEVEL - highLevel
        val y = COMPONENT_HIGHEST_LEVEL - highLevel
        val width = this.width - highLevel - x
        val height = this.height - highLevel - y

        val shape = this.style.shape.createShape(x, y, width, height)

        if (highLevel > 0)
        {
            val shadowShape = this.style.shape.createShape(x + highLevel, y + highLevel, width, height)
            val area = Area(shadowShape)
            area.subtract(Area(shape))
            graphics.color = SHADOW
            graphics.fill(area)
        }

        this.style.background.applyOnShape(graphics2D, shape)
        val margin = this.style.shape.margin(x, y, width, height)
        val clip = graphics2D.clip
        val xInner = x + margin.left
        val yInner = y + margin.top
        val widthInner = width - margin.left - margin.right
        val heightInner = height - margin.top - margin.bottom
        graphics2D.clip = Rectangle(xInner, yInner, widthInner, heightInner)
        this.innerComponentDraw(graphics2D, xInner, yInner, widthInner, heightInner)
        graphics2D.clip = clip
        graphics2D.color = this.style.borderColor
        this.style.shape.draw(x, y, width, height, graphics2D)
    }

    protected abstract fun innerComponentDraw(graphics2D : Graphics2D, x : Int, y : Int, width : Int, height : Int)
    protected abstract fun innerComponentMinimumSize() : Dimension
    protected abstract fun innerComponentPreferredSize() : Dimension
    protected abstract fun innerComponentRefresh()

    protected fun refresh()
    {
        this.innerComponentRefresh()

        var size = this.innerComponentMinimumSize()
        var margin = this.style.shape.margin(0, 0, size.width, size.height)
        this.minimumSize = Dimension(size.width + margin.left + margin.right + COMPONENT_HIGHEST_LEVEL,
                                     size.height + margin.top + margin.bottom + COMPONENT_HIGHEST_LEVEL)

        size = this.innerComponentPreferredSize()
        margin = this.style.shape.margin(0, 0, size.width, size.height)
        this.preferredSize = Dimension(size.width + margin.left + margin.right + COMPONENT_HIGHEST_LEVEL,
                                       size.height + margin.top + margin.bottom + COMPONENT_HIGHEST_LEVEL)

        this.repaint()
    }
}
