package khelp.game.model.states

import fr.jhelp.utilities.random
import khelp.game.model.Statistics
import khelp.utilities.extensions.bounds
import kotlin.math.max

open class State
{
    val enabled : Boolean get() = this.turnLeft > 0

    var turnLeft : Int = 0
        private set

    var baseResistance : Int = 0
        set(value)
        {
            field = value.bounds(0, 100)
        }

    var additionalResistance : Int = 0
        set(value)
        {
            field = value.bounds(0, 100)
        }

    fun nextTurn(statistics : Statistics) : Int
    {
        this.turnLeft = max(0, this.turnLeft - 1)

        var effect = this.effectOnTurn(statistics)

        if (this.turnLeft == 0)
        {
            effect = this.effectLastTurn(statistics)
        }

        return effect
    }

    fun remove()
    {
        this.turnLeft = 0
    }

    fun apply(numberTurn : Int)
    {
        if (random(0, 100) >= this.baseResistance + this.additionalResistance)
        {
            this.turnLeft = max(1, numberTurn)
        }
    }

    protected open fun effectOnTurn(statistics : Statistics) : Int = 0
    protected open fun effectLastTurn(statistics : Statistics) : Int = 0
}
