package khelp.ui.style

open class StyleImageWithText : StyleText()
{
    var imageTextRelativePosition : ImageTextRelativePosition = ImageTextRelativePosition.IMAGE_LEFT_OF_TEXT
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
