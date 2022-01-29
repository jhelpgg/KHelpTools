package khelp.game.model.states

import fr.jhelp.utilities.random
import khelp.game.model.Statistics

class Death : State()
{
    override fun effectLastTurn(statistics : Statistics) : Int =
        if (random(0, 100) > statistics.luck)
        {
            - statistics.healthPoint
        }
        else
        {
            0
        }
}
