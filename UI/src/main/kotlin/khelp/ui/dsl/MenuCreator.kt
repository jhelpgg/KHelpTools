package khelp.ui.dsl

import khelp.ui.GenericAction
import javax.swing.Icon
import javax.swing.JMenu
import javax.swing.JMenuItem

class MenuCreator(text : String)
{
    internal val menu = JMenu(text)

    val separator get() = this.menu.addSeparator()

    operator fun String.invoke(clickAction : () -> Unit)
    {
        val menuItem = JMenuItem(this)
        menuItem.addActionListener { clickAction() }
        this@MenuCreator.menu.add(menuItem)
    }

    operator fun String.invoke(icon : Icon, clickAction : () -> Unit)
    {
        val menuItem = JMenuItem(this, icon)
        menuItem.addActionListener { clickAction() }
        this@MenuCreator.menu.add(menuItem)
    }

    operator fun Icon.invoke(clickAction : () -> Unit)
    {
        val menuItem = JMenuItem(this)
        menuItem.addActionListener { clickAction() }
        this@MenuCreator.menu.add(menuItem)
    }

    operator fun GenericAction.unaryPlus()
    {
        this@MenuCreator.menu.add(JMenuItem(this))
    }
}
