package khelp.match3.game

import khelp.match3.ui.EventMouse
import khelp.match3.ui.EventMouseType
import khelp.match3.ui.MouseManager
import khelp.thread.TaskContext
import khelp.thread.flow.Flow
import khelp.ui.TextAlignment
import khelp.ui.game.GameImage
import khelp.utilities.provider.provided
import java.awt.Color
import java.awt.Font

object TitleScreen
{
    private val gameScreen by provided<GameImage>()
    private val mouseManager by provided<MouseManager>()
    private var mouseFlow : Flow<Unit>? = null

    fun showScreen()
    {
        this.mouseFlow = this.mouseManager.eventMouseFlow.then(TaskContext.INDEPENDENT, this::eventMouse)

        this.gameScreen.drawPercent { percentGraphics ->
            percentGraphics.color = Color.BLACK
            percentGraphics.font = Font("Courier", Font.BOLD, 32)
            percentGraphics.drawText(0.5, 0.5, "Click to start", TextAlignment.CENTER)
        }
    }

    fun hideScreen()
    {
        this.mouseFlow?.cancel()
    }

    fun canCloseNow() : Boolean
    {
        return true
    }

    private fun eventMouse(eventMouse : EventMouse)
    {
        if (eventMouse.eventMouseType == EventMouseType.CLiCK)
        {
            GameStateMachine.gameState = GameState.GAME_SCREEN
        }
    }
}
