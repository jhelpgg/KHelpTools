package khelp.ui.style.shape

import java.awt.Graphics2D
import java.awt.Insets
import java.awt.Shape
import java.awt.geom.RoundRectangle2D

object StyleShapeRoundRectangle : StyleShape
{
    private const val arc = 32
    private const val margin = this.arc / 8

    override fun draw(x : Int, y : Int, width : Int, height : Int, graphics2D : Graphics2D)
    {
        graphics2D.drawRoundRect(x+1, y+1, width - 2, height - 2, this.arc, this.arc)
    }

    override fun margin(x : Int, y : Int, width : Int, height : Int) : Insets =
        Insets(this.margin, this.margin, this.margin, this.margin)

    override fun createShape(x : Int, y : Int, width : Int, height : Int) : Shape =
        RoundRectangle2D.Double(x.toDouble(), y.toDouble(),
                                width.toDouble(), height.toDouble(),
                                this.arc.toDouble(), this.arc.toDouble())
}