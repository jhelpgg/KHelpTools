package khelp.ui.game

import khelp.ui.utilities.centerOnScreen
import khelp.ui.utilities.getScreenBounds
import khelp.ui.utilities.packedSize
import java.awt.BorderLayout
import java.awt.event.WindowEvent
import javax.swing.JFrame
import kotlin.system.exitProcess

class GameFrame(width : Int, height : Int, title : String = "GameFrame") : JFrame(title)
{
    companion object
    {
        fun fullScreenFrame(title : String = "GameFrame") : GameFrame
        {
            val screenBounds = getScreenBounds(0)
            return GameFrame(screenBounds.width, screenBounds.height, title)
        }
    }

    var canCloseNow : () -> Boolean = { true }
    val gameComponent = GameComponent(width, height)

    init
    {
        this.defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE
        this.isUndecorated = true
        this.isAlwaysOnTop = true
        this.layout = BorderLayout()
        this.add(this.gameComponent, BorderLayout.CENTER)
        packedSize(this)
        centerOnScreen(this)
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
}
