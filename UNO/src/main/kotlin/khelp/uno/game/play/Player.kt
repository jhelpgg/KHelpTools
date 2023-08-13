package khelp.uno.game.play

import khelp.uno.model.CardStack

class Player(val human : Boolean)
{
    var coins : Int = 1000
    val stack : CardStack = CardStack()
    var blocked : Boolean = false

    val canPlay : Boolean get() = this.coins > 0 && ! this.blocked

    fun initialize()
    {
        this.coins = 1000
        this.blocked = false
        this.stack.clear()
    }
}
