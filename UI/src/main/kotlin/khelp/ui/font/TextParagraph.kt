package khelp.ui.font

import java.awt.Dimension

class TextParagraph(val textLines : List<TextLine>, val size : Dimension)
{
    val string : String by lazy {
        val stringBuilder = StringBuilder()
        var notFirst = false

        for (line in this.textLines)
        {
            if (notFirst)
            {
                stringBuilder.append('\n')
            }

            stringBuilder.append(line)
            notFirst = true
        }

        stringBuilder.toString()
    }
}
