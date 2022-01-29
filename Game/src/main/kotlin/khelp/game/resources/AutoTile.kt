package khelp.game.resources

import khelp.ui.extensions.drawPart
import khelp.ui.game.GameImage
import java.awt.Graphics2D
import kotlin.math.max

class AutoTile(path : String, timeTransition : Int = 1)
{
    companion object
    {
        const val TOTAL_WIDTH = TileSet.TILE_SIZE * 3
    }

    private val image = GameImage.load(path, GameResources.resources)
    private val numberAnimation = this.image.width / AutoTile.TOTAL_WIDTH
    private val timeTransition = max(1, timeTransition)

    fun draw(x : Int, y : Int, graphics2D : Graphics2D, autoTileType : AutoTileType, frame : Int)
    {
        val additional = ((frame / this.timeTransition) % this.numberAnimation) * AutoTile.TOTAL_WIDTH
        graphics2D.drawPart(x, y,
                            autoTileType.tileX * TileSet.TILE_SIZE + additional,
                            autoTileType.tileY * TileSet.TILE_SIZE,
                            TileSet.TILE_SIZE, TileSet.TILE_SIZE,
                            this.image)
    }
}
