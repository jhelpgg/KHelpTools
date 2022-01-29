package khelp.game.screen.play

import khelp.game.resources.TileSet
import java.awt.Graphics2D

class PlayScreenLayer(val numberTileHorizontal : Int, val numberTileVertical : Int)
{
    private val tiles =
        Array<TileDescription>(this.numberTileHorizontal * this.numberTileVertical) { EmptyTileDescription }

    operator fun get(x : Int, y : Int) : TileDescription = this.tiles[x + y * this.numberTileHorizontal]

    operator fun set(x : Int, y : Int, tileDescription : TileDescription)
    {
        this.tiles[x + y * this.numberTileHorizontal] = tileDescription
    }

    fun draw(x : Int, y : Int, graphics2D : Graphics2D, frame : Int)
    {
        var tile = 0

        for (tileY in 0 until this.numberTileVertical)
        {
            for (tileX in 0 until this.numberTileHorizontal)
            {
                this.tiles[tile].draw(x + tileX * TileSet.TILE_SIZE, y + tileY * TileSet.TILE_SIZE, graphics2D, frame)
                tile ++
            }
        }
    }
}
