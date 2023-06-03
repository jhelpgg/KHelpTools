package khelp.ui.style

import khelp.ui.TextAlignment
import khelp.ui.utilities.TITLE_FONT

fun title() : StyleImageWithTextClickable {
    val style = StyleImageWithTextClickable()
    style.font = TITLE_FONT
    style.textAlignment = TextAlignment.CENTER
    return style
}

