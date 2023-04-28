package khelp.game.model

import khelp.utilities.extensions.bounds
import khelp.utilities.math.random
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class Statistics(maxHealthPoint : Int, maxMagicPoint : Int,
                 attack : Int, defence : Int, speed : Int, agility : Int, intelligence : Int, luck : Int)
{
    companion object
    {
        const val MINIMUM_MAX_HEALTH_POINT = 123
        const val MINIMUM_MAX_MAGIC_POINT = 12
        const val STATISTIC_MAXIMUM = 99

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

    var maxHealthPoint = max(Statistics.MINIMUM_MAX_HEALTH_POINT, maxHealthPoint)
        set(value)
        {
            field = max(Statistics.MINIMUM_MAX_HEALTH_POINT, value)
            this.healthPoint = min(this.healthPoint, field)
        }

    var healthPoint = this.maxHealthPoint
        set(value)
        {
            field = value.bounds(0, this.maxHealthPoint)
        }

    var maxMagicPoint = max(maxMagicPoint, Statistics.MINIMUM_MAX_MAGIC_POINT)
        set(value)
        {
            field = max(Statistics.MINIMUM_MAX_MAGIC_POINT, value)
            this.magicPoint = min(this.magicPoint, field)
        }

    var magicPoint = this.maxMagicPoint
        set(value)
        {
            field = value.bounds(0, this.maxMagicPoint)
        }

    var attack = attack.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }

    var defence = defence.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }

    var magicDefence = defence.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }

    var speed = speed.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }

    var agility = agility.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }

    var intelligence = intelligence.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }

    var luck = luck.bounds(1, Statistics.STATISTIC_MAXIMUM)
        set(value)
        {
            field = value.bounds(1, Statistics.STATISTIC_MAXIMUM)
        }
}
