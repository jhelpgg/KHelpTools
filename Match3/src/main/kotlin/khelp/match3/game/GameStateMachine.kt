package khelp.match3.game

object GameStateMachine
{
    var gameState : GameState = GameState.JUST_LAUNCHED
        set(value)
        {
            val oldState = field
            val needRefresh = field != value
            field = value

            if (needRefresh)
            {
                this.refresh(oldState)
            }
        }

    private fun refresh(oldState : GameState)
    {
        when (oldState)
        {
            GameState.TITLE_SCREEN -> TitleScreen.hideScreen()
            GameState.GAME_SCREEN  -> GameScreen.hideScreen()
            else                   -> Unit
        }

        when (this.gameState)
        {
            GameState.TITLE_SCREEN -> TitleScreen.showScreen()
            GameState.GAME_SCREEN  -> GameScreen.showScreen()
            else                   -> Unit
        }
    }

    fun canCloseNow() : Boolean =
        when (this.gameState)
        {
            GameState.TITLE_SCREEN -> TitleScreen.canCloseNow()
            GameState.GAME_SCREEN  -> GameScreen.canCloseNow()
            else                   -> true
        }
}
