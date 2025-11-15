/**
 * @file Actor.kt
 * Base class for all game entities that can participate in combat.
 * Defines the common properties and behaviors for players, enemies, and NPCs.
 */
package khelp.game.model

import khelp.game.model.states.Death
import khelp.game.model.states.Mute
import khelp.game.model.states.Poison
import khelp.game.model.states.Regen
import khelp.game.model.states.Stone
import khelp.game.resources.GameResources
import khelp.ui.extensions.drawPart
import khelp.ui.game.GameImage
import java.awt.Graphics2D
import java.awt.Point

/**
 * Abstract base class for all combat participants in the game.
 *
 * An Actor represents any entity that can:
 * - Participate in turn-based combat
 * - Have statistics (HP, MP, attack, defense, etc.)
 * - Be affected by status effects
 * - Appear in the turn order display
 *
 * @property statistics The combat statistics for this actor
 * @property turnFacePath Path to the image file containing the actor's face for turn display
 * @property turnFacePosition Position within the face image (x,y coordinates)
 * @property team True if this actor is on the player's team, false for enemies
 *
 * @see Statistics for combat calculations
 * @see TurnOrder for turn-based combat management
 */
abstract class Actor(val statistics : Statistics,
                     private val turnFacePath : String,
                     private val turnFacePosition : Point,
                     val team : Boolean)
{
    /** Death status effect - instant KO chance at end of duration */
    val death : Death = Death()

    /** Mute status effect - prevents magic/skill usage */
    val mute : Mute = Mute()

    /** Poison status effect - damage over time */
    val poison : Poison = Poison()

    /** Regen status effect - healing over time */
    val regen : Regen = Regen()

    /** Stone status effect - prevents all actions */
    val stone : Stone = Stone()

    /**
     * Draws the actor's face in the turn order display.
     *
     * The face is a 32x32 pixel image extracted from the turnFacePath
     * at the position specified by turnFacePosition.
     *
     * @param x X coordinate to draw at
     * @param y Y coordinate to draw at
     * @param graphics2D Graphics context to draw on
     */
    fun drawTurnFace(x : Int, y : Int, graphics2D : Graphics2D)
    {
        graphics2D.drawPart(x, y,
                            this.turnFacePosition.x, this.turnFacePosition.y,
                            TURN_FACE_SIZE, TURN_FACE_SIZE,
                            GameImage.load(this.turnFacePath, GameResources.resources))
    }

    /**
     * Processes the actor's turn, applying all active status effects.
     *
     * Called at the start of each of the actor's turns to:
     * - Apply damage/healing from status effects
     * - Decrement status effect durations
     * - Trigger end-of-duration effects
     *
     * @return Pair of (positive effects total, negative effects total)
     */
    fun nextTurn() : Pair<Int, Int>
    {
        var positiveEffect = 0
        var negativeEffect = 0

        if (this.death.enabled)
        {
            negativeEffect += this.death.nextTurn(this.statistics)
        }

        if (this.mute.enabled)
        {
            negativeEffect += this.mute.nextTurn(this.statistics)
        }

        if (this.poison.enabled)
        {
            negativeEffect += this.poison.nextTurn(this.statistics)
        }

        if (this.regen.enabled)
        {
            positiveEffect += this.regen.nextTurn(this.statistics)
        }

        if (this.stone.enabled)
        {
            negativeEffect += this.stone.nextTurn(this.statistics)
        }

        return Pair(positiveEffect, negativeEffect)
    }
}