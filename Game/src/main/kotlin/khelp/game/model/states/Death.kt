package khelp.game.model.states

import khelp.utilities.math.random
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
