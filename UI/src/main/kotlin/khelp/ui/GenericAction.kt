package khelp.ui

import khelp.resources.ResourcesText
import khelp.thread.TaskContext
import khelp.ui.extensions.toGameImage
import khelp.ui.game.GameImage
import khelp.utilities.log.verbose
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.Icon
import javax.swing.KeyStroke

class GenericAction(private var resourcesText : ResourcesText,
                    private var keyName : String,
                    smallImage : GameImage?,
                    largeImage : GameImage?,
                    private var keyToolTip : String?,
                    private val taskContext : TaskContext,
                    private val actionClick : () -> Unit) : AbstractAction()
{
    constructor(resourcesText : ResourcesText,
                keyName : String,
                image : GameImage?,
                keyToolTip : String?,
                taskContext : TaskContext,
                actionClick : () -> Unit)
            : this(resourcesText, keyName, image, image, keyToolTip, taskContext, actionClick)

    constructor(resourcesText : ResourcesText,
                keyName : String,
                smallImage : GameImage?,
                largeImage : GameImage?,
                taskContext : TaskContext,
                actionClick : () -> Unit)
            : this(resourcesText, keyName, smallImage, largeImage, null, taskContext, actionClick)

    constructor(resourcesText : ResourcesText,
                keyName : String,
                image : GameImage?,
                taskContext : TaskContext,
                actionClick : () -> Unit)
            : this(resourcesText, keyName, image, image, null, taskContext, actionClick)

    constructor(resourcesText : ResourcesText,
                keyName : String,
                keyToolTip : String?,
                taskContext : TaskContext,
                actionClick : () -> Unit)
            : this(resourcesText, keyName, null, null, keyToolTip, taskContext, actionClick)

    constructor(resourcesText : ResourcesText,
                keyName : String,
                taskContext : TaskContext,
                actionClick : () -> Unit)
            : this(resourcesText, keyName, null, null, null, taskContext, actionClick)

    init
    {
        (smallImage ?: largeImage)?.let { image -> this.smallImage(image) }
        (largeImage ?: smallImage)?.let { image -> this.largeImage(image) }
        this.resourcesText.observableChange.observedBy(TaskContext.INDEPENDENT) { this.updateTexts() }
    }

    fun keyName() : String = this.keyName

    fun keyName(keyName : String)
    {
        this.keyName = keyName
        this.putValue(Action.NAME, keyName)
    }

    fun name() : String = this.getValue(Action.NAME) as String

    fun keyToolTip() : String? = this.keyToolTip

    fun keyToolTip(keyToolTip : String)
    {
        this.keyToolTip = keyToolTip
        this.putValue(Action.SHORT_DESCRIPTION, keyToolTip)
    }

    fun removeKeyToolTip()
    {
        this.keyToolTip = null
        this.putValue(Action.SHORT_DESCRIPTION, null)
    }

    fun toolTip() : String? = this.getValue(Action.SHORT_DESCRIPTION) as String?

    fun smallAndLargeImage(image : GameImage)
    {
        this.smallImage(image)
        this.largeImage(image)
    }

    fun removeImages()
    {
        this.removeSmallImage()
        this.removeLargeImage()
    }

    fun smallImage() : GameImage? = this.getValue(Action.SMALL_ICON) as GameImage?

    fun smallImage(smallImage : GameImage)
    {
        this.putValue(Action.SMALL_ICON, smallImage)
    }

    fun removeSmallImage()
    {
        this.putValue(Action.SMALL_ICON, null)
    }

    fun largeImage() : GameImage? = this.getValue(Action.LARGE_ICON_KEY) as GameImage?

    fun largeImage(largeImage : GameImage)
    {
        this.putValue(Action.LARGE_ICON_KEY, largeImage)
    }

    fun removeLargeImage()
    {
        this.putValue(Action.LARGE_ICON_KEY, null)
    }

    fun shortCut() : KeyStroke? = this.getValue(Action.ACCELERATOR_KEY) as KeyStroke?

    fun shortCut(keyStroke : KeyStroke)
    {
        this.putValue(Action.ACCELERATOR_KEY, keyStroke)
    }

    fun removeShortCut()
    {
        this.putValue(Action.ACCELERATOR_KEY, null)
    }

    override fun actionPerformed(actionEvent : ActionEvent)
    {
        this.taskContext.parallel(this.actionClick)
    }

    override fun putValue(key : String, newValue : Any?)
    {
        var value : Any? = newValue

        when
        {
            Action.NAME == key                                       ->
                if (value == null)
                {
                    throw NullPointerException("Value for NAME must not be null")
                }
                else
                {
                    this.keyName = value.toString()
                    value = this.resourcesText[this.keyName]
                }
            Action.SHORT_DESCRIPTION == key                          ->
                if (value != null)
                {
                    this.keyToolTip = value.toString()
                    value = this.resourcesText[this.keyToolTip !!]
                }
                else
                {
                    this.keyToolTip = null
                }
            Action.SMALL_ICON == key || Action.LARGE_ICON_KEY == key ->
                if (value != null)
                {
                    if (value !is Icon)
                    {
                        throw IllegalArgumentException(
                            "A ${Icon::class.java.name} or a ${GameImage::class.java.name} should be associate to the key $key, not a ${value.javaClass.name}")
                    }

                    value = value.toGameImage()
                }
            Action.ACCELERATOR_KEY != key && "enabled" != key        ->
            {
                verbose("The key $key is not managed here!")
                return
            }
        }

        super.putValue(key, value)
    }

    private fun updateTexts()
    {
        this.putValue(Action.NAME, this.keyName)
        this.putValue(Action.SHORT_DESCRIPTION, this.keyToolTip)
    }
}
