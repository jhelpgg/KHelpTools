package khelp.engine3d.gui.dsl

import khelp.engine3d.gui.component.menu.GUIMenu
import khelp.ui.GenericAction

class GUIMenuCreator internal constructor(private val menu : GUIMenu)
{
    operator fun GenericAction.unaryPlus()
    {
        this@GUIMenuCreator.menu.add(this)
    }
}
