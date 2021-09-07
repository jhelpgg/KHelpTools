package khelp.ui.extensions

import khelp.ui.game.GameImage
import javax.swing.Icon

fun Icon.toGameImage() : GameImage
{
    if (this is GameImage)
    {
        return this
    }

    val width = this.iconWidth
    val height = this.iconHeight

    if (width <= 0 || height <= 0)
    {
        return GameImage.DUMMY
    }

    val gameImage = GameImage(width, height)
    gameImage.draw { graphics2D -> this.paintIcon(null, graphics2D, 0, 0) }
    return gameImage
}
