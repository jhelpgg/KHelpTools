package khelp.engine3d.animation

import khelp.engine3d.render.Texture
import khelp.ui.game.GameImage
import khelp.ui.game.interpolation.GameImageInterpolation
import khelp.ui.game.interpolation.GameImageInterpolationMelt
import khelp.ui.game.interpolation.GameImageInterpolationType
import khelp.utilities.math.Percent
import kotlin.math.max

class AnimationTexture(start : GameImage, end : GameImage,
                       transitionMillisecond : Long,
                       gameImageInterpolationType : GameImageInterpolationType = GameImageInterpolationMelt) : Animation()
{
    private val transitionMillisecond = max(1L, transitionMillisecond)
    private val gameImageInterpolation = GameImageInterpolation(start, end, gameImageInterpolationType)
    val texture = Texture(this.gameImageInterpolation.result)

    override fun animate(time : Long) : Boolean
    {
        this.gameImageInterpolation.percent(Percent(time, this.transitionMillisecond))
        return time <= this.transitionMillisecond
    }
}
