package khelp.ui.dsl

import khelp.resources.ResourcesText
import khelp.thread.TaskContext
import khelp.ui.components.style.StyledButton
import khelp.ui.game.GameImage
import khelp.ui.style.StyleImageWithTextClickable

class StyledButtonCreator(val keyText : String, val resourcesText : ResourcesText)
{
    private var taskContext : TaskContext = TaskContext.INDEPENDENT
    private var onClick : (StyledButton) -> Unit = {}
    private val style : StyleImageWithTextClickable = StyleImageWithTextClickable()
    var image : GameImage = GameImage.DUMMY

    fun onClick(taskContext : TaskContext = TaskContext.INDEPENDENT, action : (StyledButton) -> Unit)
    {
        this.taskContext = taskContext
        this.onClick = action
    }

    fun style(styleCreator : StyleImageWithTextClickable.() -> Unit)
    {
        styleCreator(this.style)
    }

    fun create() : StyledButton
    {
        val button = StyledButton(this.keyText, this.resourcesText, this.style)
        button.image = this.image
        button.onClick(this.taskContext, this.onClick)
        return button
    }
}
