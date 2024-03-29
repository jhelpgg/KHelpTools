package khelp.ui.components

import khelp.resources.ResourcesText
import khelp.thread.TaskContext
import khelp.thread.delay
import khelp.ui.GenericAction
import khelp.ui.components.message.Message
import khelp.ui.components.message.MessageDialog
import khelp.ui.dsl.ActionCreator
import khelp.ui.dsl.MenuBarCreator
import khelp.ui.utilities.centerOnScreen
import khelp.ui.utilities.packedSize
import khelp.ui.utilities.takeAllScreen
import java.awt.BorderLayout
import java.awt.event.WindowEvent
import javax.swing.JComponent
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.KeyStroke
import kotlin.system.exitProcess

class JHelpFrame internal constructor(title : String = "JHelpFrame",
                                      decorated : Boolean = true, private val full : Boolean = false)
    : JFrame(title)
{
    private val dialogs = HashMap<String, JDialog>()
    private val actions = HashMap<String, GenericAction>()
    private val messages = HashMap<String, Message>()

    var canCloseNow : () -> Boolean = { true }

    init
    {
        this.defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE
        this.isUndecorated = ! decorated
        this.layout = BorderLayout()
    }

    fun menuBar(creator : MenuBarCreator.() -> Unit)
    {
        val menuBarCreator = MenuBarCreator()
        creator(menuBarCreator)
        this.jMenuBar = menuBarCreator.menuBar
    }

    fun dialog(dialogId : String, dialogCreator : JDialog.() -> Unit)
    {
        val dialog = JDialog(this)
        dialogCreator(dialog)
        this.dialogs[dialogId] = dialog
    }

    fun globalAction(actionId : String, keyName : String, resourcesText : ResourcesText, shortcut : KeyStroke,
                     creator : ActionCreator.() -> Unit)
    {
        val actionCreator = ActionCreator(keyName, resourcesText)
        creator(actionCreator)
        val action = actionCreator.create()
        action.shortCut(shortcut)
        val root = this.rootPane
        val actionMap = root.actionMap
        val inputMap = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        actionMap.put(action.keyName(), action)
        inputMap.put(shortcut, action.keyName())
        this.actions[actionId] = action
    }

    fun action(actionId : String, keyName : String, resourcesText : ResourcesText, creator : ActionCreator.() -> Unit)
    {
        val actionCreator = ActionCreator(keyName, resourcesText)
        creator(actionCreator)
        this.actions[actionId] = actionCreator.create()
    }

    fun action(actionId : String) : GenericAction =
        this.actions[actionId] !!

    fun message(messageId : String, resourcesText : ResourcesText, creator : Message.() -> Unit)
    {
        val message = Message(resourcesText)
        creator(message)
        this.messages[messageId] = message
    }

    fun showDialog(dialogId : String)
    {
        this.dialogs[dialogId]?.let { dialog ->
            if (! dialog.isVisible)
            {
                dialog.pack()
                dialog.setLocation(this.x + (this.width - dialog.width) / 2, this.y + (this.height - dialog.height) / 2)
                dialog.isVisible = true
            }
        }
    }

    fun hideDialog(dialogId : String)
    {
        this.dialogs[dialogId]?.isVisible = false
    }

    fun showMessage(messageId : String)
    {
        this.messages[messageId]?.let { message -> this.showMessage(message) }
    }

    fun showMessage(message : Message)
    {
        MessageDialog.showMessage(message)
    }

    override fun processWindowEvent(windowEvent : WindowEvent)
    {
        when (windowEvent.id)
        {
            WindowEvent.WINDOW_CLOSING -> this.close()
            else                       -> super.processWindowEvent(windowEvent)
        }
    }

    fun close()
    {
        if (this.canCloseNow())
        {
            this.dispose()
            exitProcess(0)
        }
    }

    fun showFrame()
    {
        if (this.full)
        {
            takeAllScreen(this)
        }
        else
        {
            packedSize(this)
        }

        centerOnScreen(this)
        this.isVisible = true

        // Force do readjust frame
        this.invalidate()
        delay(TaskContext.MAIN, 128) { this.revalidate() }
    }
}
