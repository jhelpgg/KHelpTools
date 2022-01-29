package khelp.game.model

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D

const val TURN_FACE_SIZE = 32
private const val NUMBER_VISIBLE_ORDER = 8

object TurnOrder
{
    const val width = NUMBER_VISIBLE_ORDER * TURN_FACE_SIZE
    const val height = TURN_FACE_SIZE
    private val visibleOrder = Array<Actor>(NUMBER_VISIBLE_ORDER) { EmptyActor }
    private var currentOrder : MutableList<Actor> = ArrayList()
    val currentActor : Actor get() = this.visibleOrder[0]
    var fightStatus : FightStatus = FightStatus.FIGHTING
        private set

    fun initializeFight(actors : List<Actor>)
    {
        this.currentOrder = Statistics.fightOrder(actors)
        this.update()
    }

    fun draw(x : Int, y : Int, graphics2D : Graphics2D)
    {
        graphics2D.stroke = BasicStroke(3f)
        graphics2D.color = Color.YELLOW
        var xx = x

        for (actor in this.visibleOrder)
        {
            actor.drawTurnFace(xx, y, graphics2D)
            graphics2D.drawRect(xx, y, TURN_FACE_SIZE, TURN_FACE_SIZE)
            xx += TURN_FACE_SIZE
        }
    }

    fun nextTurn()
    {
        this.currentOrder.add(this.currentOrder[0])
        this.currentOrder.removeAt(0)
        this.update()
    }

    /**
     * Call it when some die, revive, stoned, un-stoned
     */
    fun update()
    {
        var hasOpponent = false
        var hasTeam = false

        for (index in 0 until NUMBER_VISIBLE_ORDER)
        {
            var notAdded = true

            do
            {
                val actor = this.currentOrder[0]
                this.currentOrder.add(actor)
                this.currentOrder.removeAt(0)

                if (! actor.stone.enabled && actor.statistics.healthPoint > 0)
                {
                    if (actor.team)
                    {
                        hasTeam = true
                    }
                    else
                    {
                        hasOpponent = true
                    }

                    this.visibleOrder[index] = actor
                    notAdded = false
                }
            }
            while (notAdded)
        }

        this.fightStatus =
            when
            {
                ! hasOpponent -> FightStatus.WIN
                ! hasTeam     -> FightStatus.LOST
                else          -> FightStatus.FIGHTING
            }
    }
}