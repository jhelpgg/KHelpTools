package khelp.ui.dsl

import javax.swing.JMenuBar

class MenuBarCreator
{
    internal val menuBar = JMenuBar()

    fun menu(text : String, creator : MenuCreator.() -> Unit)
    {
        val menuCreator = MenuCreator(text)
        creator(menuCreator)
        this.menuBar.add(menuCreator.menu)
    }
}
