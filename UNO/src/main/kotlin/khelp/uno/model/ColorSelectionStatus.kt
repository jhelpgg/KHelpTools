package khelp.uno.model

import khelp.ui.game.GameImage
import khelp.uno.resourcesUno

enum class ColorSelectionStatus(private val path : String)
{
    NO_SELECTION(path = "images/ColorSelectorBase.png"),
    SELECT_BLUE(path = "images/ColorSelectorBlue.png"),
    SELECT_GREEN(path = "images/ColorSelectorGreen.png"),
    SELECT_RED(path = "images/ColorSelectorRed.png"),
    SELECT_YELLOW(path = "images/ColorSelectorYellow.png")
    ;

    val image : GameImage by lazy { GameImage.load(this.path, resourcesUno) }
}