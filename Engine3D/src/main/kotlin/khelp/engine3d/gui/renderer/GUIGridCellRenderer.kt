package khelp.engine3d.gui.renderer

import khelp.engine3d.gui.component.GUIComponent

fun interface GUIGridCellRenderer<V : Any>
{
    fun component(cellValue : V) : GUIComponent
}
