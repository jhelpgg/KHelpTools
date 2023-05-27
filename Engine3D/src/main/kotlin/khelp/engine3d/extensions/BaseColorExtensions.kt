package khelp.engine3d.extensions

import khelp.engine3d.render.Color4f
import khelp.ui.utilities.colors.BaseColor

val BaseColor<*>.color4f : Color4f get() = Color4f(this.argb)
