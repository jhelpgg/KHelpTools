package khelp.ui.components

import khelp.resources.ResourcesText
import khelp.ui.GenericAction
import khelp.ui.dsl.ActionCreator
import khelp.ui.dsl.MenuBarCreator
import khelp.ui.utilities.centerOnScreen
import khelp.ui.utilities.packedSize
import khelp.ui.utilities.takeAllScreen
import java.awt.BorderLayout
import java.awt.event.WindowEvent
import javax.swing.JDialog
import javax.swing.JFrame
import kotlin.system.exitProcess

class JHelpFrame internal constructor(title : String = "JHelpFrame",
                                      decorated : Boolean = true, private val full : Boolean = false)
    : JFrame(title)
{
    private val dialogs = HashMap<String, JDialog>()
    private val actions = HashMap<String, GenericAction>()
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

    fun action(actionId : String, keyName : String, resourcesText : ResourcesText, creator : ActionCreator.() -> Unit)
    {
        val actionCreator = ActionCreator(keyName, resourcesText)
        creator(actionCreator)
        this.actions[actionId] = actionCreator.create()
    }

    fun action(actionId : String) : GenericAction =
        this.actions[actionId] !!

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
    }
}
