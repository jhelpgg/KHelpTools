package  khelp.game.screen.play

import khelp.game.resources.AutoTile
import khelp.game.resources.AutoTileType
import khelp.game.resources.TileSet
import java.awt.Graphics2D

sealed class TileDescription
{
    abstract fun draw(x : Int, y : Int, graphics2D : Graphics2D, frame : Int)
}

object EmptyTileDescription : TileDescription()
{
    override fun draw(x : Int, y : Int, graphics2D : Graphics2D, frame : Int) = Unit
}

class TileSetDescription(private val tileSet : TileSet,
                         private val tileX : Int, private val tileY : Int)
    : TileDescription()
{
    override fun draw(x : Int, y : Int, graphics2D : Graphics2D, frame : Int)
    {
        this.tileSet.draw(x, y, graphics2D, this.tileX, this.tileY)
    }
}

class AutoTileDescription(private val autoTile : AutoTile,
                          private val autoTileType : AutoTileType)
    : TileDescription()
{
    override fun draw(x : Int, y : Int, graphics2D : Graphics2D, frame : Int)
    {
        this.autoTile.draw(x, y, graphics2D, this.autoTileType, frame)
    }
}