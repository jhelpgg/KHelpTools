package khelp.game.model.states

import khelp.game.model.Statistics

class Poison : State()
{
    override fun effectOnTurn(statistics : Statistics) : Int =
        - ((statistics.healthPoint + 9) / 10)
}
