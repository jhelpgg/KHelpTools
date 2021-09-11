package khelp.ui.components.style

import khelp.resources.ResourcesText
import khelp.ui.extensions.drawImage
import khelp.ui.game.GameImage
import khelp.ui.style.ImageTextRelativePosition
import khelp.ui.style.StyleImageWithText
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Point
import kotlin.math.max

open class StyledLabel<SIWT: StyleImageWithText>(keyText : String, resourcesText : ResourcesText, style : SIWT)
    : StyledTextComponent<SIWT>(style, keyText, resourcesText)
{
    companion object
    {
        private const val SPACE = 8
        private const val SIZE = StyledLabel.SPACE * 2
    }

    var image : GameImage = GameImage.DUMMY
        set(value)
        {
            field = value
            this.refresh()
        }

    override fun innerTextSize(nakedSize : Dimension) : Dimension =
        when (this.style.imageTextRelativePosition)
        {
            ImageTextRelativePosition.IMAGE_LEFT_OF_TEXT, ImageTextRelativePosition.IMAGE_RIGHT_OF_TEXT   ->
                Dimension(this.image.width + StyledLabel.SIZE,
                          max(nakedSize.height, this.image.height + StyledLabel.SIZE))

            ImageTextRelativePosition.IMAGE_ABOVE_OF_TEXT, ImageTextRelativePosition.IMAGE_BELLOW_OF_TEXT ->
                Dimension(max(nakedSize.width, this.image.width + StyledLabel.SIZE),
                          this.image.height + StyledLabel.SIZE)

            ImageTextRelativePosition.IMAGE_UNDER_TEXT                                                    ->
                Dimension(max(nakedSize.width, this.image.width + StyledLabel.SIZE),
                          max(nakedSize.height, this.image.height + StyledLabel.SIZE))
        }

    override fun innerTextDraw(graphics2D : Graphics2D, x : Int, y : Int, width : Int, height : Int) : Point
    {
        if (this.image == GameImage.DUMMY)
        {
            return Point(x + (width - this.textSize.width) / 2, y + (height - this.textSize.height) / 2)
        }

        return when (this.style.imageTextRelativePosition)
        {
            ImageTextRelativePosition.IMAGE_LEFT_OF_TEXT   ->
                this.drawImageLeft(graphics2D, x, y, width, height)

            ImageTextRelativePosition.IMAGE_RIGHT_OF_TEXT  ->
                this.drawImageRight(graphics2D, x, y, width, height)

            ImageTextRelativePosition.IMAGE_ABOVE_OF_TEXT  ->
                this.drawImageTop(graphics2D, x, y, width, height)

            ImageTextRelativePosition.IMAGE_BELLOW_OF_TEXT ->
                this.drawImageBottom(graphics2D, x, y, width, height)

            ImageTextRelativePosition.IMAGE_UNDER_TEXT     ->
                this.drawImageCenter(graphics2D, x, y, width, height)
        }
    }

    private fun drawImageLeft(graphics2D : Graphics2D, x : Int, y : Int, width : Int, height : Int) : Point
    {
        val w = StyledLabel.SPACE + this.image.width + StyledLabel.SPACE + this.textSize.width + StyledLabel.SPACE
        val xx = x + (width - w) / 2
        graphics2D.drawImage(xx,
                             y + (height - this.image.height) / 2,
                             this.image)
        return Point(xx + StyledLabel.SPACE + this.image.width, y + (height - this.textSize.height) / 2)
    }

    private fun drawImageRight(graphics2D : Graphics2D, x : Int, y : Int, width : Int, height : Int) : Point
    {
        val w = StyledLabel.SPACE + this.image.width + StyledLabel.SPACE + this.textSize.width + StyledLabel.SPACE
        val xx = (width - w) / 2
        graphics2D.drawImage(xx + StyledLabel.SPACE + this.textSize.width + StyledLabel.SPACE,
                             y + (height - this.image.height) / 2,
                             this.image)
        return Point(xx, y + (height - this.textSize.height) / 2)
    }

    private fun drawImageTop(graphics2D : Graphics2D, x : Int, y : Int, width : Int, height : Int) : Point
    {
        val h = StyledLabel.SPACE + this.image.height + StyledLabel.SPACE + this.textSize.height + StyledLabel.SPACE
        val yy = y + (height - h) / 2
        graphics2D.drawImage(x + (width - this.image.width) / 2,
                             yy,
                             this.image)
        return Point(x + (width - this.textSize.width) / 2, yy + StyledLabel.SPACE + this.image.height)
    }

    private fun drawImageBottom(graphics2D : Graphics2D, x : Int, y : Int, width : Int, height : Int) : Point
    {
        val h = StyledLabel.SPACE + this.image.height + StyledLabel.SPACE + this.textSize.height + StyledLabel.SPACE
        val yy = (height - h) / 2
        graphics2D.drawImage(x + (width - this.image.width) / 2,
                             yy + StyledLabel.SPACE + this.textSize.height + StyledLabel.SPACE,
                             this.image)
        return Point(x + (width - this.textSize.width) / 2, yy)
    }

    private fun drawImageCenter(graphics2D : Graphics2D, x : Int, y : Int, width : Int, height : Int) : Point
    {
        graphics2D.drawImage(x + (width - this.image.width) / 2,
                             y + (height - this.image.height) / 2,
                             this.image)
        return Point(x + (width - this.textSize.width) / 2, y + (height - this.textSize.height) / 2)
    }
}
