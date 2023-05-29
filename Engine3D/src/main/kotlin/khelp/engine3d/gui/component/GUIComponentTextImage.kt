package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUIMargin
import khelp.resources.ResourcesText
import khelp.resources.defaultTexts
import khelp.ui.TextAlignment
import khelp.ui.extensions.drawText
import khelp.ui.font.JHelpFont
import khelp.ui.game.GameImage
import khelp.ui.style.ImageTextRelativePosition
import khelp.ui.utilities.DEFAULT_FONT
import khelp.utilities.extensions.bounds
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import kotlin.math.max

class GUIComponentTextImage : GUIComponent()
{
    var keyText = "ok"
    var resourcesText : ResourcesText = defaultTexts
    var font : JHelpFont = DEFAULT_FONT
    var textColorMain : Color = Color.WHITE
    var textColorBorder : Color = Color.BLACK
    var textAlignment : TextAlignment = TextAlignment.LEFT
    var image : GameImage = GameImage.DARK_LIGHT
    var imageTextRelativePosition : ImageTextRelativePosition = ImageTextRelativePosition.IMAGE_LEFT_OF_TEXT
    var separatorSpaceSize : Int = 4
        set(value)
        {
            field = value.bounds(0, 16)
        }
    var imageSize : Int = 64
        set(value)
        {
            field = value.bounds(16, 128)
        }


    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        val parentWidth = this.width - margin.width - 8
        val parentHeight = this.height - margin.height - 8
        val x = margin.left + 4
        val y = margin.top +4

        val imageX : Int
        val imageY : Int
        val textX : Int
        val textY : Int
        val textWidth: Int
        val textHeight : Int
        val centerVertical : Boolean

        when (this.imageTextRelativePosition)
        {
            ImageTextRelativePosition.IMAGE_LEFT_OF_TEXT   ->
            {
                imageX = x
                imageY = y + (parentHeight - this.imageSize) / 2
                textX = x + this.imageSize + this.separatorSpaceSize
                textY = y
                textWidth = parentWidth - this.imageSize - this.separatorSpaceSize
                textHeight = parentHeight
                centerVertical = true
            }

            ImageTextRelativePosition.IMAGE_RIGHT_OF_TEXT  ->
            {
                imageX = x + parentWidth - this.imageSize
                imageY = y + (parentHeight - this.imageSize) / 2
                textX = x
                textY = y
                textWidth = parentWidth - this.imageSize - this.separatorSpaceSize
                textHeight = parentHeight
                centerVertical = true
            }

            ImageTextRelativePosition.IMAGE_ABOVE_OF_TEXT  ->
            {
                imageX = x + (parentWidth - this.imageSize) / 2
                imageY = y
                textX = x
                textY = y + this.imageSize + this.separatorSpaceSize
                textWidth = parentWidth
                textHeight = parentHeight - this.imageSize - this.separatorSpaceSize
                centerVertical = false
            }

            ImageTextRelativePosition.IMAGE_BELLOW_OF_TEXT ->
            {
                imageX = x + (parentWidth - this.imageSize) / 2
                imageY = y + parentHeight - this.imageSize
                textX = x
                textY = y
                textWidth = parentWidth
                textHeight = parentHeight - this.imageSize - this.separatorSpaceSize
                centerVertical = false
            }

            ImageTextRelativePosition.IMAGE_UNDER_TEXT     ->
            {
                imageX = x + (parentWidth - this.imageSize) / 2
                imageY = y + (parentHeight - this.imageSize) / 2
                textX = x
                textY = y
                textWidth = parentWidth
                textHeight = parentHeight
                centerVertical = true
            }
        }

        this.image.drawOn(graphics2D, imageX, imageY, this.imageSize, this.imageSize)

        if (this.keyText.isEmpty())
        {
            return
        }

        val text = this.resourcesText[this.keyText]
        val paragraph = this.font.computeTextParagraph(text, this.textAlignment, textWidth, textHeight)
        graphics2D.font = this.font.font
        val dy = if (centerVertical) (textHeight - paragraph.size.height) / 2 else 0

        if (this.textColorBorder.alpha > 0)
        {
            graphics2D.color = this.textColorBorder

            for (line in paragraph.textLines)
            {
                val dx =
                    when (this.textAlignment)
                    {
                        TextAlignment.LEFT   -> 0
                        TextAlignment.CENTER -> (textWidth - line.width) / 2
                        TextAlignment.RIGHT  -> textWidth - line.width
                    }

                for (yy in - 1 .. 1)
                {
                    for (xx in - 1 .. 1)
                    {
                        graphics2D.drawText(line.x + textX + dx + xx, line.y + textY + dy + yy,
                                            line.text, this.textAlignment)
                    }
                }
            }
        }

        graphics2D.color = this.textColorMain
        for (line in paragraph.textLines)
        {
            val dx =
                when (this.textAlignment)
                {
                    TextAlignment.LEFT   -> 0
                    TextAlignment.CENTER -> (textWidth - line.width) / 2
                    TextAlignment.RIGHT  -> textWidth - line.width
                }

            graphics2D.drawText(line.x + textX + dx, line.y + textY + dy, line.text, this.textAlignment)
        }
    }

    override fun preferredSize(margin : GUIMargin) : Dimension
    {
        val (textWidth, textHeight) =
            if (this.keyText.isEmpty())
            {
                Pair(16, 16)
            }
            else
            {
                val text = this.resourcesText[this.keyText]
                val paragraph = this.font.computeTextParagraph(text, this.textAlignment)
                val more = if (this.textColorBorder.alpha == 0) 0 else 2
                Pair(more + paragraph.size.width, more + paragraph.size.height)
            }

        val (width, height) =
            when (this.imageTextRelativePosition)
            {
                ImageTextRelativePosition.IMAGE_LEFT_OF_TEXT, ImageTextRelativePosition.IMAGE_RIGHT_OF_TEXT   ->
                    Pair(this.imageSize + this.separatorSpaceSize + textWidth,
                         max(this.imageSize, textHeight))
                ImageTextRelativePosition.IMAGE_ABOVE_OF_TEXT, ImageTextRelativePosition.IMAGE_BELLOW_OF_TEXT ->
                    Pair(max(this.imageSize, textWidth),
                         this.imageSize + this.separatorSpaceSize + textHeight)
                ImageTextRelativePosition.IMAGE_UNDER_TEXT                                                    ->
                    Pair(max(this.imageSize, textWidth),
                         max(this.imageSize, textHeight))
            }

        val shapeMargin = this.shape.margin(0, 0, width, height)
        return Dimension(margin.width + width + shapeMargin.left + shapeMargin.right + 16,
                         margin.height + height + shapeMargin.top + shapeMargin.bottom + 16)
    }
}
