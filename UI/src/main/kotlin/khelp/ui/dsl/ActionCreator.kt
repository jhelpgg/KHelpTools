package khelp.ui.dsl

import khelp.resources.ResourcesText
import khelp.thread.TaskContext
import khelp.ui.GenericAction
import khelp.ui.game.GameImage
import javax.swing.KeyStroke

class ActionCreator(var keyName : String, private val resourcesText : ResourcesText)
{
    private var taskContext = TaskContext.INDEPENDENT
    private var clickAction : () -> Unit = {}

    var keyToolTip : String? = null

    var image : GameImage?
        get() = this.smallImage ?: this.largeImage
        set(value)
        {
            this.smallImage = value
            this.largeImage = value
        }

    var smallImage : GameImage? = null

    var largeImage : GameImage? = null

    var shortcut : KeyStroke? = null

    fun onClick(taskContext : TaskContext, clickAction : () -> Unit)
    {
        this.taskContext = taskContext
        this.clickAction = clickAction
    }

    fun create() : GenericAction
    {
        val action = GenericAction(this.resourcesText,
                                   this.keyName, this.smallImage, this.largeImage, this.keyToolTip,
                                   this.taskContext, this.clickAction)

        this.shortcut?.let { keyStroke -> action.shortCut(keyStroke) }
        return action
    }
}
