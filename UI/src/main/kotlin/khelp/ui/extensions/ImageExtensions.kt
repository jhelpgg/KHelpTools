package khelp.ui.extensions

import khelp.ui.game.GameImage
import java.awt.Image

fun Image.toGameImage() : GameImage
{
    val width = this.getWidth(null)
    val height = this.getHeight(null)

    if (width <= 0 || height <= 0)
    {
        return GameImage.DUMMY
    }

    val gameImage = GameImage(width, height)
    gameImage.draw { graphics2D -> graphics2D.drawImage(this, 0, 0, null) }
    return gameImage
}
