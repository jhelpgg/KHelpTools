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

abstract class Actor(val statistics : Statistics,
                     private val turnFacePath : String, private val turnFacePosition : Point,
                     val team : Boolean)
{
    val death : Death = Death()
    val mute : Mute = Mute()
    val poison : Poison = Poison()
    val regen : Regen = Regen()
    val stone : Stone = Stone()

    fun drawTurnFace(x : Int, y : Int, graphics2D : Graphics2D)
    {
        graphics2D.drawPart(x, y,
                            this.turnFacePosition.x, this.turnFacePosition.y,
                            TURN_FACE_SIZE, TURN_FACE_SIZE,
                            GameImage.load(this.turnFacePath, GameResources.resources))
    }

    fun nextTurn() : Pair<Int, Int>
    {
        var positiveEffect = 0
        var negativeeffect = 0

        if (this.death.enabled)
        {
            negativeeffect += this.death.nextTurn(this.statistics)
        }

        if (this.mute.enabled)
        {
            negativeeffect += this.mute.nextTurn(this.statistics)
        }

        if (this.poison.enabled)
        {
            negativeeffect += this.poison.nextTurn(this.statistics)
        }

        if (this.regen.enabled)
        {
            positiveEffect += this.regen.nextTurn(this.statistics)
        }

        if (this.stone.enabled)
        {
            negativeeffect += this.stone.nextTurn(this.statistics)
        }

        return Pair(positiveEffect, negativeeffect)
    }
}
