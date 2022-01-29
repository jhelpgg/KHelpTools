package khelp.game.model.states

import khelp.game.model.Statistics
import khelp.utilities.extensions.bounds

class Regen : State()
{
    override fun effectOnTurn(statistics : Statistics) : Int =
        (statistics.healthPoint * 8) / 100
}
