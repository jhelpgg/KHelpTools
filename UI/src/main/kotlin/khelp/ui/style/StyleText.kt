package khelp.ui.style

import khelp.ui.TextAlignment
import khelp.ui.font.JHelpFont
import khelp.ui.utilities.TEXT_FONT
import java.awt.Color

open class StyleText : Style()
{
    var textColor : Color = Color.BLACK
        set(value)
        {
            val changed = field != value
            field = value

            if (changed)
            {
                this.signalChange()
            }
        }

    var font : JHelpFont = TEXT_FONT
        set(value)
        {
            val changed = field != value
            field = value

            if (changed)
            {
                this.signalChange()
            }
        }

    var textAlignment : TextAlignment = TextAlignment.LEFT
        set(value)
        {
            val changed = field != value
            field = value

            if (changed)
            {
                this.signalChange()
            }
        }
}
