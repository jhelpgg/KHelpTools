/**
 * @file TurnOrder.kt
 * Manages the turn-based combat order display and fight status tracking.
 */
package khelp.game.model

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D

/** Size of each actor's face in the turn order display */
const val TURN_FACE_SIZE = 32

/** Number of actors visible in the turn order display */
private const val NUMBER_VISIBLE_ORDER = 8

/**
 * Singleton object managing the turn order display and fight progression.
 *
 * Displays the next 8 actors in the turn queue with their faces.
 * Automatically updates when actors die, revive, or change status.
 * Tracks overall fight status (fighting, win, lost).
 *
 * @property width Total width of the turn order display (256 pixels)
 * @property height Total height of the turn order display (32 pixels)
 * @property currentActor The actor whose turn it currently is
 * @property fightStatus Current state of the fight
 */
object TurnOrder
{
    /** Total width of the turn order display */
    const val width = NUMBER_VISIBLE_ORDER * TURN_FACE_SIZE

    /** Total height of the turn order display */
    const val height = TURN_FACE_SIZE

    /** Array of actors visible in the turn order display */
    private val visibleOrder = Array<Actor>(NUMBER_VISIBLE_ORDER) { EmptyActor }

    /** Complete turn order list (circular) */
    private var currentOrder : MutableList<Actor> = ArrayList()

    /** The actor whose turn it currently is */
    val currentActor : Actor get() = this.visibleOrder[0]

    /** Current state of the fight */
    var fightStatus : FightStatus = FightStatus.FIGHTING
        private set

    /**
     * Initializes a new fight with the given actors.
     *
     * Creates the turn order based on actor speeds and
     * populates the visible order display.
     *
     * @param actors List of all actors participating in the fight
     */
    fun initializeFight(actors : List<Actor>)
    {
        this.currentOrder = Statistics.fightOrder(actors)
        this.update()
    }

    /**
     * Draws the turn order display.
     *
     * Shows the next 8 actors in line with yellow borders.
     * The leftmost actor is the current turn.
     *
     * @param x X coordinate to draw at
     * @param y Y coordinate to draw at
     * @param graphics2D Graphics context to draw on
     */
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

    /**
     * Advances to the next turn.
     *
     * Moves the current actor to the back of the queue
     * and updates the visible display.
     */
    fun nextTurn()
    {
        this.currentOrder.add(this.currentOrder[0])
        this.currentOrder.removeAt(0)
        this.update()
    }

    /**
     * Updates the visible turn order and fight status.
     *
     * Call this when actors:
     * - Die or revive
     * - Become stoned or un-stoned
     * - Join or leave the fight
     *
     * Automatically determines win/loss conditions based on
     * remaining active actors on each team.
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

                // Only show actors that can act (alive and not stoned)
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

        // Update fight status based on remaining actors
        this.fightStatus =
            when
            {
                ! hasOpponent -> FightStatus.WIN
                ! hasTeam     -> FightStatus.LOST
                else          -> FightStatus.FIGHTING
            }
    }
}