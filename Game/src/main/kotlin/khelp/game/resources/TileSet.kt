package khelp.game.resources

import khelp.ui.extensions.drawPart
import khelp.ui.game.GameImage
import java.awt.Graphics2D

class TileSet(path : String)
{
    companion object
    {
        const val TILE_SIZE = 32
    }

    private val image = GameImage.load(path, GameResources.resources)
    val numberHorizontal = this.image.width / TileSet.TILE_SIZE
    val numberVertical = this.image.height / TileSet.TILE_SIZE

    fun draw(x : Int, y : Int, graphics2D : Graphics2D, tileX : Int, tileY : Int)
    {
        graphics2D.drawPart(x, y,
                            tileX * TileSet.TILE_SIZE, tileY * TileSet.TILE_SIZE,
                            TileSet.TILE_SIZE, TileSet.TILE_SIZE,
                            this.image)
    }
}