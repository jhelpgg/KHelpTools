package khelp.uno.game.play

enum class Turn
{
    PLAYER,
    COMPUTER_RIGHT,
    COMPUTER_FRONT,
    COMPUTER_LEFT
    ;

    fun next(trigonometricWay : Boolean) : Turn
    {
        return if (trigonometricWay)
        {
            when (this)
            {
                Turn.PLAYER         -> Turn.COMPUTER_RIGHT
                Turn.COMPUTER_RIGHT -> Turn.COMPUTER_FRONT
                Turn.COMPUTER_FRONT -> Turn.COMPUTER_LEFT
                Turn.COMPUTER_LEFT  -> Turn.PLAYER
            }
        }
        else
        {
            when (this)
            {
                Turn.PLAYER         -> Turn.COMPUTER_LEFT
                Turn.COMPUTER_LEFT  -> Turn.COMPUTER_FRONT
                Turn.COMPUTER_FRONT -> Turn.COMPUTER_RIGHT
                Turn.COMPUTER_RIGHT -> Turn.PLAYER
            }
        }
    }
}