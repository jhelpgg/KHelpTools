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

fun <P> GameImage.copy(parameter : P, action : GameImage.(P) -> Unit) : GameImage
{
    val image = this.copy()
    image.action(parameter)
    return image
}

fun GameImage.copy(action : GameImage.() -> Unit) : GameImage
{
    val image = this.copy()
    image.action()
    return image
}

fun GameImage.grayVersion() : GameImage =
    this.copy(GameImage::gray)

fun GameImage.withContrast(contrast : Double) : GameImage =
    this.copy(contrast, GameImage::contrast)

fun GameImage.bothFlipped() : GameImage =
    this.copy(GameImage::flipBoth)

fun GameImage.horizontalFlipped() : GameImage =
    this.copy(GameImage::flipHorizontal)

fun GameImage.verticalFlipped() : GameImage =
    this.copy(GameImage::flipVertical)
