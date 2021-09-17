package khelp.ui.font

import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.parallel
import khelp.ui.TextAlignment
import khelp.ui.game.GameImage
import khelp.ui.utilities.DEFAULT_FONT
import khelp.ui.utilities.FONT_RENDER_CONTEXT
import khelp.utilities.log.exception
import khelp.utilities.text.StringExtractor
import khelp.utilities.text.lastIndexOf
import java.awt.Dimension
import java.awt.Font
import java.awt.FontMetrics
import java.io.InputStream
import java.util.Collections
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.round

class JHelpFont(val font : Font, val underline : Boolean = false)
{
    companion object
    {
        /**
         * Create font
         * @param family Font family
         * @param size Font size
         * @param bold Indicates if it has to be bold
         * @param italic Indicates if it has to be italic
         */
        internal fun createFont(family : String, size : Int, bold : Boolean = false, italic : Boolean = false) : Font
        {
            var style = Font.PLAIN

            if (bold)
            {
                style = style or Font.BOLD
            }

            if (italic)
            {
                style = style or Font.ITALIC
            }

            return Font(family, style, size)
        }

        /**
         * Create a font from a stream
         *
         * @param type      Font type
         * @param stream    Stream to get the font data
         * @param size      Size of created font
         * @param bold      Bold value
         * @param italic    Italic value
         * @param underline Indicates if have to underline or not
         * @return Created font
         */
        fun createFont(type : FontType, stream : InputStream, size : Int,
                       bold : FontValue, italic : FontValue, underline : Boolean) : FutureResult<JHelpFont> =
            JHelpFont.obtainFont(type, stream, size, bold, italic, underline)
                .and(TaskContext.INDEPENDENT)
                { font ->
                    if (font == DEFAULT_FONT)
                    {
                        val newSize = if (size < 1)
                        {
                            18
                        }
                        else
                        {
                            size
                        }

                        JHelpFont("Arial", newSize, bold == FontValue.TRUE, italic == FontValue.TRUE, underline)
                    }
                    else
                    {
                        font
                    }
                }

        /**
         * Create a font from a stream
         *
         * @param type      Font type
         * @param stream    Stream to get the font data
         * @param size      Size of created font
         * @param bold      Bold value
         * @param italic    Italic value
         * @param underline Indicates if have to underline or not
         * @return Created font OR `null` if stream not a managed font
         */
        fun obtainFont(type : FontType, stream : InputStream, size : Int,
                       bold : FontValue = FontValue.AS_DEFINED, italic : FontValue = FontValue.AS_DEFINED,
                       underline : Boolean = false) : FutureResult<JHelpFont> =
            parallel(TaskContext.MAIN) {
                try
                {
                    val fontFormat =
                        if (type == FontType.TYPE1)
                        {
                            Font.TYPE1_FONT
                        }
                        else
                        {
                            Font.TRUETYPE_FONT
                        }

                    var font = Font.createFont(fontFormat, stream)
                    val fontSize = font.size
                    val fontStyle = font.style

                    var style = 0

                    when (bold)
                    {
                        FontValue.FALSE      -> Unit
                        FontValue.AS_DEFINED -> style = style or (fontStyle and Font.BOLD)
                        FontValue.TRUE       -> style = style or Font.BOLD
                    }

                    when (italic)
                    {
                        FontValue.FALSE      -> Unit
                        FontValue.AS_DEFINED -> style = style or (fontStyle and Font.ITALIC)
                        FontValue.TRUE       -> style = style or Font.ITALIC
                    }

                    if (fontSize != size || style != fontStyle)
                    {
                        if (fontSize == size)
                        {
                            font = font.deriveFont(style)
                        }
                        else if (style == fontStyle)
                        {
                            font = font.deriveFont(size.toFloat())
                        }
                        else
                        {
                            font = font.deriveFont(style, size.toFloat())
                        }
                    }

                    JHelpFont(font, underline)
                }
                catch (exception : Exception)
                {
                    exception(exception, "Failed to create the font")
                    DEFAULT_FONT
                }
            }
    }

    /**Font measure metrics*/
    private lateinit var fontMetrics : FontMetrics
    val fontHeight : Int get() = this.fontMetrics.height

    /**
     * Font size
     */
    val size : Int = this.font.size

    /**
     * Indicates if font is bold
     *
     * @return `true` if font is bold
     */
    val bold : Boolean = this.font.style and Font.BOLD != 0

    /**
     * Indicates if font is italic
     *
     * @return `true` if font is italic
     */
    val italic : Boolean = this.font.style and Font.ITALIC != 0

    val family : String = this.font.family

    /**Maximum width of a character*/
    private val maximumCharacterWidth : Int by lazy {
        var maximum = 0

        for (character in 32 .. 127)
        {
            maximum = max(maximum, this.fontMetrics.charWidth(character))
        }

        maximum
    }

    init
    {
        GameImage.DUMMY.draw { graphics2D -> this@JHelpFont.fontMetrics = graphics2D.getFontMetrics(this.font) }
    }

    /**
     * Create font with detailed parameters
     * @param family Font family name
     * @param size Font size
     * @param bold Indicates if have to bold
     * @param italic Indicates if have to italic
     * @param underline Indicates if have to underline
     */
    constructor(family : String, size : Int, bold : Boolean = false, italic : Boolean = false,
                underline : Boolean = false) :
            this(JHelpFont.createFont(family, size, bold, italic), underline)

    /**
     * Compute text lines representation with this font with alpha mask
     *
     * @param text        Text to use
     * @param textAlign   Align to use
     * @param limitWidth  Number maximum of pixels in width
     * @param limitHeight Limit height in pixels
     * @param trim        Indicates if it has to trim lines
     * @return The couple of the list of each computed lines and the total size of all lines together
     */
    fun computeTextParagraph(text : String, textAlign : TextAlignment,
                             limitWidth : Int = Int.MAX_VALUE, limitHeight : Int = Int.MAX_VALUE,
                             trim : Boolean = true) : TextParagraph
    {
        val limit = max(this.maximumCharacterWidth + 2, limitWidth)

        val textLines = ArrayList<TextLine>()
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
                line = line.trim { it <= ' ' }
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
                    size.width = max(size.width, width)

                    textLines.add(TextLine(head, 0, size.height, width, height, false))

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

            size.width = max(size.width, width)

            textLines.add(TextLine(line !!, 0, size.height, width, height, true))

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
                TextAlignment.CENTER -> textLine.x = (size.width - textLine.width) shr 1
                TextAlignment.LEFT   -> textLine.x = 0
                TextAlignment.RIGHT  -> textLine.x = size.width - textLine.width
            }
        }

        size.width = max(1, size.width)
        size.height = max(1, size.height)
        textLines.trimToSize()
        return TextParagraph(Collections.unmodifiableList(textLines), size)
    }


    /**
     * Compute size of a string
     *
     * @param string String to measure
     * @return String size
     */
    fun stringSize(string : String) : Dimension
    {
        val bounds = this.font.getStringBounds(string, FONT_RENDER_CONTEXT)
        return Dimension(ceil(bounds.width).toInt(), ceil(bounds.height).toInt())
    }

    /**
     * Compute string width
     *
     * @param string String to measure
     * @return String width
     */
    fun stringWidth(string : String) : Int
    {
        val bounds = this.font.getStringBounds(string, FONT_RENDER_CONTEXT)
        return ceil(bounds.width).toInt()
    }

    /**
     * Compute underline position
     *
     * @param string String
     * @param y      Y of top
     * @return Y result
     */
    fun underlinePosition(string : String, y : Int) : Int
    {
        val lineMetrics = this.font.getLineMetrics(string, FONT_RENDER_CONTEXT)

        return round(y.toFloat() + lineMetrics.underlineOffset + lineMetrics.ascent).toInt()
    }

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is JHelpFont)
        {
            return false
        }

        return this.underline == other.underline && this.font == other.font
    }
}
