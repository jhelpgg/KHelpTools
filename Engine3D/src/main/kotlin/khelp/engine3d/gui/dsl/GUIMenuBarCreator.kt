package khelp.engine3d.gui.dsl

import khelp.engine3d.gui.component.menu.GUIMenuBar

class GUIMenuBarCreator internal constructor(private val menuBar : GUIMenuBar)
{
    operator fun String.invoke(menuCreator : GUIMenuCreator.() -> Unit)
    {
        val menu = this@GUIMenuBarCreator.menuBar.addMenu(this)
        val creator = GUIMenuCreator(menu)
        menuCreator(creator)
    }
}
