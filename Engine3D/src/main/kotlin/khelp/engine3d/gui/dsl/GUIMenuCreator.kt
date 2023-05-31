package khelp.engine3d.gui.dsl

import khelp.engine3d.gui.component.menu.GUIMenu
import khelp.thread.TaskContext
import khelp.ui.GenericAction
import khelp.ui.game.GameImage

class GUIMenuCreator internal constructor(private val menu : GUIMenu)
{
    operator fun GenericAction.unaryPlus()
    {
        this@GUIMenuCreator.menu.add(this)
    }

    fun action(keyText : String, action : () -> Unit)
    {
        this.menu.add(GenericAction(this.menu.resourcesText, keyText, TaskContext.INDEPENDENT, action))
    }

    fun action(keyText : String, imagePath : String, action : () -> Unit)
    {
        this.menu.add(GenericAction(this.menu.resourcesText, keyText,
                                    GameImage.Companion.load(imagePath, this.menu.resourcesText.resources),
                                    TaskContext.INDEPENDENT, action))
    }
}
