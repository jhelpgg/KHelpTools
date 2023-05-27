package khelp.ui.extensions

import khelp.ui.TextAlignment
import khelp.ui.font.JHelpFont
import khelp.ui.game.GameImage
import khelp.ui.utilities.colors.BaseColor
import java.awt.Graphics2D
import kotlin.math.max

fun Graphics2D.drawText(x : Int, y : Int, text : String, textAlignment : TextAlignment = TextAlignment.LEFT)
{
    val fontMetrics = this.getFontMetrics(this.font)
    val accent = fontMetrics.ascent
    val height = fontMetrics.height
    val lines = text.split('\n')
    val widths = IntArray(lines.size) { index -> fontMetrics.stringWidth(lines[index]) }
    val maxWidth = widths.maxOrNull() !!
    var xx : Int
    var yy = y + accent

    for ((index, line) in lines.withIndex())
    {
        val width = widths[index]

        xx = when (textAlignment)
        {
            TextAlignment.LEFT   -> x
            TextAlignment.CENTER -> x + (maxWidth - width) / 2
            TextAlignment.RIGHT  -> x + maxWidth - width
        }

        this.drawString(line, xx, yy)
        yy += height
    }
}

fun Graphics2D.fillText(x : Int, y : Int, text : String, textAlignment : TextAlignment = TextAlignment.LEFT,
                        limitHorizontal : Int = Int.MAX_VALUE)
{
    val font = JHelpFont(this.font)
    val height = font.fontHeight
    val lines = ArrayList<Pair<String, Int>>()
    var maxWidth = 0

    for (line in text.trim()
        .replace("\t", "   ")
        .split('\n'))
    {
        var lineToAdd = line
        var width = font.stringWidth(lineToAdd)
        var indexSpace = lineToAdd.lastIndexOf(' ')

        while (width > limitHorizontal && indexSpace > 0)
        {
            val firstPart = line.substring(0, indexSpace)
                .trim()
            width = font.stringWidth(firstPart)

            if (width <= limitHorizontal)
            {
                maxWidth = max(maxWidth, width)
                lines.add(Pair(firstPart, width))
                lineToAdd = lineToAdd.substring(indexSpace + 1)
                    .trim()
                width = font.stringWidth(lineToAdd)
                indexSpace = lineToAdd.lastIndexOf(' ')
            }
            else
            {
                indexSpace = lineToAdd.lastIndexOf(' ', indexSpace - 1)
            }
        }

        maxWidth = max(maxWidth, width)
        lines.add(Pair(lineToAdd, width))
    }

    var xx : Int
    var yy = y
    val space = font.stringWidth(" ")

    for ((line, width) in lines)
    {
        xx =
            when (textAlignment)
            {
                TextAlignment.LEFT   -> x
                TextAlignment.CENTER -> x + (maxWidth - width) / 2
                TextAlignment.RIGHT  -> x + maxWidth - width
            }

        for (character in line)
        {
            if (character > ' ')
            {
                val shape = font.shape(character.toString(), xx, yy)
                this.fill(shape)
                xx += shape.bounds.width
            }
            else
            {
                xx += space
            }
        }

        yy += height
    }
}

fun Graphics2D.drawImage(x : Int, y : Int, image : GameImage)
{
    image.drawOn(this, x, y)
}

fun Graphics2D.drawImage(x : Int, y : Int, width : Int, height : Int, image : GameImage)
{
    image.drawOn(this, x, y, width, height)
}

fun Graphics2D.drawPart(x : Int, y : Int, imageX : Int, imageY : Int, width : Int, height : Int, image : GameImage)
{
    image.drawOnPart(this, x, y, imageX, imageY, width, height)
}

fun Graphics2D.drawImageCenter(x : Int, y : Int, image : GameImage)
{
    image.drawOn(this, x - image.width / 2, y - image.height - 2)
}

fun Graphics2D.baseColor(baseColor : BaseColor<*>)
{
    this.color = baseColor.color
}
