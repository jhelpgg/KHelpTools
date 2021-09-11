package khelp.ui.extensions

import khelp.ui.TextAlignment
import khelp.ui.game.GameImage
import java.awt.Graphics2D

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

fun Graphics2D.drawImage(x : Int, y : Int, image : GameImage)
{
    image.drawOn(this, x, y)
}

fun Graphics2D.drawImage(x : Int, y : Int, width : Int, height : Int, image : GameImage)
{
    image.drawOn(this, x, y, width, height)
}
