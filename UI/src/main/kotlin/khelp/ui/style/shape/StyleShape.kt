package khelp.ui.style.shape

import java.awt.Graphics2D
import java.awt.Insets
import java.awt.Shape

interface StyleShape
{
    fun draw(x:Int, y:Int, width:Int, height:Int, graphics2D : Graphics2D)

    fun margin(x:Int,y:Int,width:Int,height:Int) : Insets

    fun createShape(x:Int,y:Int,width:Int,height:Int) : Shape
}