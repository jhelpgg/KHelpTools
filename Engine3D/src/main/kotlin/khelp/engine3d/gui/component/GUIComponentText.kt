package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUIMargin
import khelp.resources.ResourcesText
import khelp.resources.defaultTexts
import khelp.ui.TextAlignment
import khelp.ui.VerticalAlignment
import khelp.ui.extensions.drawText
import khelp.ui.font.JHelpFont
import khelp.ui.utilities.DEFAULT_FONT
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D

class GUIComponentText : GUIComponent()
{
    var keyText = "ok"
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }

    var resourcesText : ResourcesText = defaultTexts
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }

    var font : JHelpFont = DEFAULT_FONT
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }

    var textColorMain : Color = Color.WHITE
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }

    var textColorBorder : Color = Color.BLACK
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }


    var textAlignment : TextAlignment = TextAlignment.LEFT
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }

    var verticalAlignment : VerticalAlignment = VerticalAlignment.TOP
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }

    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        if (this.keyText.isEmpty())
        {
            return
        }

        val text = this.resourcesText[this.keyText]
        val paragraph = this.font.computeTextParagraph(text, this.textAlignment, this.width, this.height)
        val x = this.margin.left
        graphics2D.font = this.font.font

        val y =
            when (this.verticalAlignment)
            {
                VerticalAlignment.TOP    -> margin.top
                VerticalAlignment.CENTER -> margin.top + (this.height - margin.height - paragraph.size.height) / 2
                VerticalAlignment.BOTTOM -> this.height - margin.bottom - paragraph.size.height
            }

        if (this.textColorBorder.alpha > 0)
        {
            graphics2D.color = this.textColorBorder

            for (line in paragraph.textLines)
            {
                val dx =
                    when (this.textAlignment)
                    {
                        TextAlignment.LEFT   -> 0
                        TextAlignment.CENTER -> (this.width - margin.width - line.width) / 2
                        TextAlignment.RIGHT  -> this.width - margin.width - line.width
                    }

                for (yy in - 1 .. 1)
                {
                    for (xx in - 1 .. 1)
                    {
                        graphics2D.drawText(line.x + xx + x + dx, line.y + yy + y, line.text, this.textAlignment)
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
                    TextAlignment.CENTER -> (this.width - margin.width - line.width) / 2
                    TextAlignment.RIGHT  -> this.width - margin.width - line.width
                }

            graphics2D.drawText(line.x + x + dx, line.y + y, line.text, this.textAlignment)
        }
    }

    override fun preferredSize(margin : GUIMargin) : Dimension
    {
        if (this.keyText.isEmpty())
        {
            return Dimension(16 + margin.width, 16 + margin.height)
        }

        val text = this.resourcesText[this.keyText]
        val paragraph = this.font.computeTextParagraph(text, this.textAlignment)
        val more = if (this.textColorBorder.alpha == 0) 0 else 2
        return Dimension(margin.width + more + paragraph.size.width,
                         margin.height + more + paragraph.size.height)
    }
}
