package khelp.ui.style.shape

import java.awt.Graphics2D
import java.awt.Insets
import java.awt.Rectangle
import java.awt.Shape

object StyleShapeRectangle : StyleShape
{
    override fun draw(x : Int, y : Int, width : Int, height : Int, graphics2D : Graphics2D)
    {
        graphics2D.drawRect(x+1, y+1, width - 2, height - 2)
    }

    override fun margin(x : Int, y : Int, width : Int, height : Int) : Insets =
        Insets(4, 4, 4, 4)

    override fun createShape(x : Int, y : Int, width : Int, height : Int) : Shape =
        Rectangle(x, y, width, height)
}