package khelp.ui.style

import khelp.ui.style.background.StyleBackground
import khelp.ui.style.background.StyleBackgroundColor
import khelp.ui.style.background.StyleBackgroundTransparent
import java.awt.Color

class StyleImageWithTextClickable : StyleImageWithText()
{
    var outOfBackground : StyleBackground = StyleBackgroundTransparent
        set(value)
        {
            val changed = field != value
            field = value

            if (changed)
            {
                this.signalChange()
            }
        }

    var overBackground : StyleBackground = StyleBackgroundColor(Color.CYAN)
        set(value)
        {
            val changed = field != value
            field = value

            if (changed)
            {
                this.signalChange()
            }
        }

    var clickBackground : StyleBackground = StyleBackgroundColor(Color.CYAN)
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
