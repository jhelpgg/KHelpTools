package khelp.image.extensions

import khelp.image.JHelpImage
import khelp.image.JHelpTextLineAlpha
import khelp.image.WHITE
import khelp.ui.TextAlignment
import khelp.ui.font.JHelpFont
import khelp.ui.utilities.FONT_RENDER_CONTEXT
import khelp.utilities.text.StringExtractor
import khelp.utilities.text.lastIndexOf
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.util.Collections
import kotlin.math.max

fun JHelpFont.computeTextLinesAlpha(
    text : String, textAlign : TextAlignment, limitWidth : Int = Int.MAX_VALUE,
    limitHeight : Int = Int.MAX_VALUE, trim : Boolean = true) : Pair<List<JHelpTextLineAlpha>, Dimension>
{
    val limit = max(this.maximumCharacterWidth + 2, limitWidth)

    val textLines = ArrayList<JHelpTextLineAlpha>()
    val lines = StringExtractor(text, "\n\r", "", "")
    val size = Dimension()

    var width : Int
    var index : Int
    var start : Int
    val height = this.fontHeight

    var line = lines.next()
    var head : String
    var tail : String

    while (line != null)
    {
        if (trim)
        {
            line = line.trim({ it <= ' ' })
        }

        width = this.stringWidth(line)
        index = line.length - 1

        while (width > limit && index > 0)
        {
            start = index
            index = lastIndexOf(line !!, index, ' ', '\t', '\'', '&', '~', '"', '#', '{', '(', '[', '-', '|',
                                '`', '_', '\\', '^', '@', '°', ')', ']',
                                '+', '=', '}', '"', 'µ', '*', ',', '?', '.', ';', ':', '/', '!', '§', '<',
                                '>', '²')

            if (index >= 0)
            {
                if (trim)
                {
                    head = line.substring(0, index)
                        .trim { it <= ' ' }
                    tail = line.substring(index)
                        .trim { it <= ' ' }
                }
                else
                {
                    head = line.substring(0, index)
                    tail = line.substring(index)
                }
            }
            else
            {
                start --
                index = start
                head = line.substring(0, index) + "-"
                tail = line.substring(index)
            }

            width = this.stringWidth(head)

            if (width <= limit)
            {
                size.width = Math.max(size.width, width)

                textLines.add(JHelpTextLineAlpha(head, 0, size.height, width, height,
                                                 this.createImage(head, WHITE, 0),
                                                 false))

                size.height += height

                line = tail
                width = this.stringWidth(line)
                index = line.length - 1

                if (size.height >= limitHeight)
                {
                    break
                }
            }
            else
            {
                index --
            }
        }

        if (size.height >= limitHeight)
        {
            break
        }

        size.width = Math.max(size.width, width)

        textLines.add(JHelpTextLineAlpha(line !!, 0, size.height, width, height,
                                         this.createImage(line, WHITE, 0),
                                         true))

        size.height += height

        if (size.height >= limitHeight)
        {
            break
        }

        line = lines.next()
    }

    for (textLine in textLines)
    {
        when (textAlign)
        {
            TextAlignment.CENTER -> textLine.x = size.width - textLine.width shr 1
            TextAlignment.LEFT   -> textLine.x = 0
            TextAlignment.RIGHT  -> textLine.x = size.width - textLine.width
        }
    }

    size.width = Math.max(1, size.width)
    size.height = Math.max(1, size.height)
    return Pair(Collections.unmodifiableList(textLines), size)
}

/**
 * Create image with text draw on it.
 *
 * Image size fit exactly to the text
 *
 * @param string     Text to create its image
 * @param foreground Foreground color
 * @param background Background color
 * @return Created image
 */
fun JHelpFont.createImage(string : String, foreground : Int, background : Int) : JHelpImage
{
    val width = max(1, this.stringWidth(string))
    val height = max(1, this.fontHeight)
    val ascent = this.ascent

    val nb = width * height
    var pixels = IntArray(nb)

    for (i in 0 until nb)
    {
        pixels[i] = background
    }

    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    bufferedImage.setRGB(0, 0, width, height, pixels, 0, width)
    val graphics2d = bufferedImage.createGraphics()
    this.applyHints(graphics2d)
    graphics2d.color = Color(foreground, true)
    graphics2d.font = this.font
    graphics2d.drawString(string, 0, ascent)

    if (this.underline)
    {
        val y = this.underlinePosition(string, 0)
        graphics2d.drawLine(0, y, width, y)
    }

    pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width)
    val image = JHelpImage(width, height, pixels)
    graphics2d.dispose()
    bufferedImage.flush()
    return image
}

fun JHelpFont.applyHints(graphics2d : Graphics2D)
{
    graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)

    if (FONT_RENDER_CONTEXT.isAntiAliased())
    {
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    }
    else
    {
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF)
    }

    graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
    graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
    graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

    if (FONT_RENDER_CONTEXT.usesFractionalMetrics())
    {
        graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                                    RenderingHints.VALUE_FRACTIONALMETRICS_ON)
    }
    else
    {
        graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                                    RenderingHints.VALUE_FRACTIONALMETRICS_OFF)
    }
}