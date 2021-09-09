package khelp.ui.border

import java.awt.Color
import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Insets
import java.awt.geom.Path2D
import java.awt.geom.RoundRectangle2D
import javax.swing.border.AbstractBorder
import kotlin.math.max

class RoundedBorder(val color : Color = Color.BLACK, thickness : Int = 1) : AbstractBorder()
{
    companion object
    {
        const val ARC = 16
    }

    val thickness = max(1, thickness)

    override fun paintBorder(component : Component, graphics : Graphics, x : Int, y : Int, width : Int, height : Int)
    {
        val graphics2D : Graphics2D = graphics as Graphics2D
        val oldColor = graphics2D.color
        graphics2D.color = this.color

        if (this.thickness == 1)
        {
            graphics2D.drawRoundRect(x, y, width - 1, height - 1, RoundedBorder.ARC, RoundedBorder.ARC)
        }
        else
        {
            val arc = RoundedBorder.ARC.toDouble()
            val offset = this.thickness.toDouble()
            val size = offset * 2.0
            val xx = x.toDouble()
            val yy = y.toDouble()
            val ww = width.toDouble()
            val hh = height.toDouble()
            val outer = RoundRectangle2D.Double(xx, yy, ww, hh, arc, arc)
            val inner = RoundRectangle2D.Double(xx + offset, yy + offset, ww - size, hh - size, arc, arc)
            val path = Path2D.Double(Path2D.WIND_EVEN_ODD)
            path.append(outer, false)
            path.append(inner, false)
            graphics2D.fill(path)
        }

        graphics2D.color = oldColor
    }

    override fun getBorderInsets(component : Component, insets : Insets) : Insets
    {
        val margin = 3*(RoundedBorder.ARC) / 8
        insets.set(margin, margin, margin, margin)
        return insets
    }

    override fun isBorderOpaque() : Boolean = true
}
