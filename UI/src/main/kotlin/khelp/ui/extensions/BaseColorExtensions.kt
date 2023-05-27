package khelp.ui.extensions

import khelp.ui.utilities.colors.BaseColor
import java.awt.Color

val BaseColor<*>.color : Color get() = Color(this.argb, true)
