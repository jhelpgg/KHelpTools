package khelp.ui.style.background

import java.awt.Graphics2D
import java.awt.Paint
import java.awt.Shape
import java.util.Objects

class StyleBackgroundPaint(private val paint : Paint) : StyleBackground
{
    override fun applyOnShape(graphics2D : Graphics2D, shape : Shape)
    {
        val paint = graphics2D.paint
        graphics2D.fill(shape)
        graphics2D.paint = paint
    }

    override fun hashCode() : Int = Objects.hash(this.paint)

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is StyleBackgroundPaint)
        {
            return false
        }

        return this.paint == other.paint
    }
}
