package khelp.game.screen

import khelp.ui.game.GameImage

abstract class GameScreen
{
    val image = GameImage(640, 320)

    abstract fun startScreen()
    abstract fun pauseScreen()
    abstract fun refresh()
}