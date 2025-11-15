/**
 * @file State.kt
 * Base class for all status effects in the game.
 * Provides common functionality for duration-based effects.
 */
package khelp.game.model.states

import khelp.utilities.math.random
import khelp.game.model.Statistics
import khelp.utilities.extensions.bounds
import kotlin.math.max

/**
 * Base class for all status effects that can affect actors during combat.
 *
 * Status effects have:
 * - Duration in turns
 * - Resistance calculation
 * - Per-turn effects
 * - End-of-duration effects
 *
 * Subclasses implement specific effects like poison damage or regeneration.
 *
 * @property enabled True if the effect is currently active (turnLeft > 0)
 * @property turnLeft Number of turns remaining for this effect
 * @property baseResistance Base resistance to this effect (0-100%)
 * @property additionalResistance Additional resistance from equipment/buffs (0-100%)
 *
 * @see Death
 * @see Poison
 * @see Regen
 * @see Mute
 * @see Stone
 */
open class State
{
    /** True if the effect is currently active */
    val enabled : Boolean get() = this.turnLeft > 0

    /** Number of turns remaining for this effect */
    var turnLeft : Int = 0
        private set

    /** Base resistance to this effect (0-100%) */
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
