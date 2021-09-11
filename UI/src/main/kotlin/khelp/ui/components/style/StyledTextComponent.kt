package khelp.ui.components.style

import khelp.resources.ResourcesText
import khelp.thread.TaskContext
import khelp.thread.delay
import khelp.ui.extensions.drawText
import khelp.ui.font.TextParagraph
import khelp.ui.style.StyleText
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Point

abstract class StyledTextComponent<ST : StyleText>(textStyle : ST, keyText : String,
                                                   private val resourcesText : ResourcesText)
    : StyledComponent<ST>(textStyle)
{
    var keyText : String = keyText
        set(value)
        {
            field = value
            this.refresh()
        }

    var text : String
        get() = this.textParagraph.string
        set(value)
        {
            this.keyText = ""
            this.textParagraph = this.style.font.computeTextParagraph(value, this.style.textAlignment)
            this.refresh()
        }

    private var textParagraph : TextParagraph = TextParagraph(emptyList(), Dimension(0, 0))
    protected val textSize : Dimension get() = this.textParagraph.size

    init
    {
        delay(128) { this.resourcesText.observableChange.observedBy(TaskContext.INDEPENDENT) { this.refresh() } }
    }

    protected abstract fun innerTextDraw(graphics2D : Graphics2D, x : Int, y : Int, width : Int, height : Int) : Point
    protected abstract fun innerTextSize(nakedSize : Dimension) : Dimension

    final override fun innerComponentRefresh()
    {
        if (this.keyText.isNotEmpty())
        {
            this.textParagraph = this.style.font.computeTextParagraph(this.resourcesText[this.keyText],
                                                                      this.style.textAlignment)
        }
    }

    final override fun innerComponentMinimumSize() : Dimension
    {
        val size = this.textParagraph.size
        val innerSize = this.innerTextSize(size)
        return Dimension(size.width + innerSize.width, size.height + innerSize.height)
    }

    final override fun innerComponentPreferredSize() : Dimension
    {
        val size = this.textParagraph.size
        val innerSize = this.innerTextSize(size)
        return Dimension(size.width + innerSize.width + 8, size.height + innerSize.height + 8)
    }

    final override fun innerComponentDraw(graphics2D : Graphics2D, x : Int, y : Int, width : Int, height : Int)
    {
        val textLocation = this.innerTextDraw(graphics2D, x, y, width, height)
        val xx = textLocation.x
        val yy = textLocation.y

        graphics2D.color = this.style.textColor
        graphics2D.font = this.style.font.font
        val textAlignment = this.style.textAlignment

        for (textLine in this.textParagraph.textLines)
        {
            graphics2D.drawText(xx + textLine.x, yy + textLine.y, textLine.text, textAlignment)
        }
    }
}
