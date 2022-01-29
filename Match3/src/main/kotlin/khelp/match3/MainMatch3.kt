package khelp.match3

import khelp.match3.game.GameState
import khelp.match3.game.GameStateMachine
import khelp.match3.ui.FrameMatch3

fun main()
{
    FrameMatch3.launchFrame()
    GameStateMachine.gameState = GameState.TITLE_SCREEN
}