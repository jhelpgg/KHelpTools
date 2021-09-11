package khelp.ui.style.shape

import khelp.ui.extensions.innerBounds
import java.awt.Graphics2D
import java.awt.Insets
import java.awt.Shape
import java.awt.geom.Ellipse2D
import java.awt.geom.RoundRectangle2D
import kotlin.math.min

object StyleShapeSausage : StyleShape
{
    override fun draw(x : Int, y : Int, width : Int, height : Int, graphics2D : Graphics2D)
    {
        graphics2D.draw(this.createShape(x, y, width, height))
    }

    override fun margin(x : Int, y : Int, width : Int, height : Int) : Insets
    {
        val shape = this.createShape(x, y, width, height)
        val outer = shape.bounds2D
        val inner = shape.innerBounds()
        return Insets((inner.y - outer.y).toInt() / 2,
                      (inner.x - outer.x).toInt() / 2,
                      (outer.maxY - inner.maxY).toInt() / 128,
                      (outer.maxX - inner.maxX).toInt() / 128)
    }


    override fun createShape(x : Int, y : Int, width : Int, height : Int) : Shape =
        when (width)
        {
            height -> Ellipse2D.Double(x.toDouble() + 1.0, y.toDouble() + 1.0,
                                       width.toDouble() - 2.0, height.toDouble() - 2.0)
            else   ->
            {
                val w = width.toDouble() - 2.0
                val h = height.toDouble() - 2.0
                val arc = min(w, h) // 2.0
                RoundRectangle2D.Double(x.toDouble() + 1.0, y.toDouble() + 1.0, w, h, arc, arc)
            }
        }
}