package khelp.uno.game

import khelp.uno.game.end.GameEndScreen
import khelp.uno.game.introduction.GameIntroductionScreen
import khelp.uno.game.play.GamePlayScreen

enum class GameStep(val gameScreen : GameScreen)
{
    INTRODUCTION(GameIntroductionScreen()),
    PLAY_GAME(GamePlayScreen()),
    END_GAME(GameEndScreen())
}
