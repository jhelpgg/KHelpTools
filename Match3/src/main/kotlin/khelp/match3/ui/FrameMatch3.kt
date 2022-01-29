package khelp.match3.ui

import khelp.match3.game.GameStateMachine
import khelp.preferences.Preferences
import khelp.thread.TaskContext
import khelp.ui.dsl.borderLayout
import khelp.ui.dsl.frame
import khelp.ui.game.GameComponent
import khelp.ui.utilities.CHARACTER_ESCAPE
import khelp.ui.utilities.createKeyStroke
import khelp.ui.utilities.screenRectangle
import khelp.utilities.provider.provideSingle

object FrameMatch3
{
    fun launchFrame()
    {
        val screenSize = screenRectangle(0).size
        val mouseManager = MouseManager(screenSize.width, screenSize.height)
        provideSingle { mouseManager }
        provideSingle { Preferences() }
        val screenComponent = GameComponent(1024, 1024)
        provideSingle { screenComponent.gameImage }
        screenComponent.addMouseListener(mouseManager)
        screenComponent.addMouseMotionListener(mouseManager)

        frame("Match 3 by JHelp", decorated = false, full = true) {
            globalAction("exit", "exit", gameTexts, createKeyStroke(CHARACTER_ESCAPE)) {
                onClick(TaskContext.INDEPENDENT) { close() }
            }

            borderLayout { center(screenComponent) }

            canCloseNow = GameStateMachine::canCloseNow
        }
    }
}
