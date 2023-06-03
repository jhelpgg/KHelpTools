package khelp.ui.style

import khelp.ui.TextAlignment
import khelp.ui.extensions.color
import khelp.ui.style.background.StyleBackgroundColor
import khelp.ui.style.shape.StyleShapeSausage
import khelp.ui.utilities.BUTTON_FONT
import khelp.ui.utilities.TITLE_FONT
import khelp.ui.utilities.TRANSPARENT
import khelp.ui.utilities.colors.Green
import khelp.ui.utilities.colors.Grey

fun title() : StyleImageWithTextClickable
{
    val style = StyleImageWithTextClickable()
    style.font = TITLE_FONT
    style.textAlignment = TextAlignment.CENTER
    style.borderColor = TRANSPARENT
    return style
}

fun button() : StyleImageWithTextClickable
{
    val style = StyleImageWithTextClickable()
    style.font = BUTTON_FONT
    style.textAlignment = TextAlignment.CENTER
    style.shape = StyleShapeSausage
    style.overBackground = StyleBackgroundColor(Green.GREEN_0300.color)
    style.outOfBackground = StyleBackgroundColor(Green.GREEN_0500.color)
    style.clickBackground = StyleBackgroundColor(Green.GREEN_0700.color)
    style.borderColor = Grey.BLACK.color
    return style
}
