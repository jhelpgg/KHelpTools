/**
 * @file Statistics.kt
 * Manages combat statistics and damage calculations for actors.
 * Core of the game's combat system.
 */
package khelp.game.model

import khelp.utilities.extensions.bounds
import khelp.utilities.math.random
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

/**
 * Represents the combat statistics for an actor.
 *
 * This class manages all combat-related values including:
 * - Health and Magic points (current and maximum)
 * - Combat stats (attack, defense, speed, etc.)
 * - Stat boundaries and validation
 *
 * All statistics are automatically bounded to valid ranges.
 *
 * @property maxHealthPoint Maximum health points (min: 123)
 * @property maxMagicPoint Maximum magic points (min: 12)
 * @property attack Physical attack power (1-99)
 * @property defence Physical defense (1-99)
 * @property speed Turn order priority (1-99)
 * @property agility Evasion chance (1-99)
 * @property intelligence Magic attack power (1-99)
 * @property luck Critical hit chance and various bonuses (1-99)
 *
 * @constructor Creates statistics with the specified values
 */
class Statistics(maxHealthPoint : Int, maxMagicPoint : Int,
                 attack : Int, defence : Int, speed : Int, agility : Int, intelligence : Int, luck : Int)
{
    companion object
    {
        /** Minimum allowed maximum health points */
        const val MINIMUM_MAX_HEALTH_POINT = 123

        /** Minimum allowed maximum magic points */
        const val MINIMUM_MAX_MAGIC_POINT = 12

        /** Maximum value for any combat statistic */
        const val STATISTIC_MAXIMUM = 99

        /**
         * Calculates physical damage for a strength-based attack.
         *
         * Damage calculation considers:
         * - Attacker's attack vs defender's defense
         * - Agility-based evasion chance
         * - Luck-based damage variance (±25%)
         * - Element-based bonus/malus
         *
         * @param attack Base attack power
         * @param fighter Attacker's statistics
         * @param receiver Defender's statistics
         * @param bonusMalus Element-based damage modifier
         * @return Hurt result (hit with damage or miss)
         */
        fun strengthAttack(attack : Int, fighter : Statistics, receiver : Statistics, bonusMalus : BonusMalus) : Hurt
        {
            val luck = max(1, receiver.luck - fighter.luck)
            val chanceToAvoid = max(1, receiver.agility - fighter.agility) + luck

            if (random(0, 200) <= chanceToAvoid)
            {
                return MissHurt
            }

            var factor = bonusMalus.factor
            val luckChoice = random(0, 100)

            if (luckChoice <= luck)
            {
                factor *= 0.75
            }
            else if (luckChoice >= (100 - luck))
            {
                factor *= 1.25
            }

            return HitHurt(max(1, ceil(attack * (fighter.attack - receiver.defence) * factor).toInt()))
        }

        /**
         * Calculates magical damage for an intelligence-based attack.
         *
         * Similar to strength attack but uses:
         * - Intelligence vs magic defense
         * - Same evasion and luck mechanics
         *
         * @param attack Base magic power
         * @param fighter Attacker's statistics
         * @param receiver Defender's statistics
         * @param bonusMalus Element-based damage modifier
         * @return Hurt result (hit with damage or miss)
         */
        fun magicAttack(attack : Int, fighter : Statistics, receiver : Statistics, bonusMalus : BonusMalus) : Hurt
        {
            val luck = max(1, receiver.luck - fighter.luck)
            val chanceToAvoid = max(1, receiver.agility - fighter.agility) + luck

            if (random(0, 200) <= chanceToAvoid)
            {
                return MissHurt
            }

            var factor = bonusMalus.factor
            val luckChoice = random(0, 100)

            if (luckChoice <= luck)
            {
                factor *= 0.75
            }
            else if (luckChoice >= (100 - luck))
            {
                factor *= 1.25
            }

            return HitHurt(max(1, ceil(attack * (fighter.intelligence - receiver.magicDefence) * factor).toInt()))
        }

        /**
         * Determines turn order for combat.
         *
         * Creates a circular turn order based on speed:
         * - Faster actors get more frequent turns
         * - The order repeats when exhausted
         *
         * @param actors List of combat participants
         * @return Mutable list representing the turn order
         */
        fun fightOrder(actors : List<Actor>) : MutableList<Actor>
        {
            val actorsList = ArrayList<Pair<Actor, Int>>()
            var minSpeed = 100

            for (actor in actors)
            {
                val speed = actor.statistics.speed
                minSpeed = min(minSpeed, speed)

                val size = actorsList.size
                var index = 0

                while (index < size)
                {
                    val (_, testSpeed) = actorsList[index]

                    if (speed > testSpeed)
                    {
                        break
                    }

                    index ++
                }

                if (index >= size)
                {
                    actorsList.add(Pair(actor, speed))
                }
                else
                {
                    actorsList.add(index, Pair(actor, speed))
                }
            }

            val order = ArrayList<Actor>()

            while (actorsList.isNotEmpty())
            {
                val (actor, range) = actorsList[0]
                actorsList.removeAt(0)
                order.add(actor)
                val left = range - minSpeed

                if (left > 0)
                {
                    actorsList.add(Pair(actor, left))
                }
            }

            return order
        }
    }

    /** Maximum health points - automatically bounded to minimum */
    var maxHealthPoint = max(Statistics.MINIMUM_MAX_HEALTH_POINT, maxHealthPoint)
        set(value)
        {
            field = max(Statistics.MINIMUM_MAX_HEALTH_POINT, value)
            this.healthPoint = min(this.healthPoint, field)
        }

    /** Current health points - automatically bounded to 0..maxHealthPoint */
    var healthPoint = this.maxHealthPoint
        set(value)
        {
            field = value.bounds(0, this.maxHealthPoint)
        }

    /** Maximum magic points - automatically bounded to minimum */
    var maxMagicPoint = max(maxMagicPoint, Statistics.MINIMUM_MAX_MAGIC_POINT)
        set(value)
        {
            field = max(Statistics.MINIMUM_MAX_MAGIC_POINT, value)
            this.magicPoint = min(this.magicPoint, field)
        }

    /** Current magic points - automatically bounded to 0..maxMagicPoint */
    var magicPoint = this.maxMagicPoint
        set(value)
        {
            field = value.bounds(0, this.maxMagicPoint)
        }

    /** Physical attack power (1-99) */
    var attack = attack.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }

    /** Physical defense (1-99) */
    var defence = defence.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }

    /** Magic defense (1-99) */
    var magicDefence = defence.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }

    /** Turn order priority (1-99) - higher means more frequent turns */
    var speed = speed.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }

    /** Evasion chance (1-99) */
    var agility = agility.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }

    /** Magic power and skill effectiveness (1-99) */
    var intelligence = intelligence.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }

    /** Affects critical hits, status resistance, and damage variance (1-99) */
    var luck = luck.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }
}