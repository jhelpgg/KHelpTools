package khelp.ui.dsl

import khelp.resources.ResourcesText
import khelp.ui.components.style.StyledLabel
import khelp.ui.game.GameImage
import khelp.ui.style.StyleImageWithText

class StyledLabelCreator(val keyText : String, val resourcesText : ResourcesText)
{
    private val style : StyleImageWithText = StyleImageWithText()
    var image : GameImage = GameImage.DUMMY

    fun style(styleCreator : StyleImageWithText.() -> Unit)
    {
        styleCreator(this.style)
    }

    fun create() : StyledLabel<StyleImageWithText>
    {
        val label = StyledLabel(this.keyText, this.resourcesText, this.style)
        label.image = this.image
        return label
    }
}
