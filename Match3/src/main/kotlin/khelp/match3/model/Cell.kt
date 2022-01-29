package khelp.match3.model

import java.awt.Graphics2D

sealed class Cell
{
    abstract fun draw(xCenter : Int, yCenter : Int, frame : Int, graphics : Graphics2D)
}

object EmptyCell : Cell()
{
    override fun draw(xCenter : Int, yCenter : Int, frame : Int, graphics : Graphics2D) = Unit
}

data class Gem(val gemType : GemType) : Cell()
{
    override fun draw(xCenter : Int, yCenter : Int, frame : Int, graphics : Graphics2D)
    {
        this.gemType.tile.draw(xCenter, yCenter, frame, graphics)
    }
}

object HorizontalArrow : Cell()
{
    override fun draw(xCenter : Int, yCenter : Int, frame : Int, graphics : Graphics2D)
    {
        horizontalLine.draw(xCenter, yCenter, frame, graphics)
    }
}

object VerticalArrow : Cell()
{
    override fun draw(xCenter : Int, yCenter : Int, frame : Int, graphics : Graphics2D)
    {
        verticalLine.draw(xCenter, yCenter, frame, graphics)
    }
}

object Bomb : Cell()
{
    override fun draw(xCenter : Int, yCenter : Int, frame : Int, graphics : Graphics2D)
    {
        bomb.draw(xCenter, yCenter, frame, graphics)
    }
}

object SameColor : Cell()
{
    private val tile = Tile("tome.png")

    override fun draw(xCenter : Int, yCenter : Int, frame : Int, graphics : Graphics2D)
    {
        this.tile.draw(xCenter, yCenter, frame, graphics)
    }
}

