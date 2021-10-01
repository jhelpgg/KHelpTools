package khelp.engine3d.animation

import khelp.engine3d.render.AnimationDSL
import khelp.engine3d.render.Node
import khelp.engine3d.render.Texture
import khelp.thread.TaskContext
import khelp.ui.game.GameImage
import khelp.ui.game.interpolation.GameImageInterpolationMelt
import khelp.ui.game.interpolation.GameImageInterpolationType

class AnimationList : Animation()
{
    private val animations = ArrayList<Animation>()
    private var animationIndex = 0

    @AnimationDSL
    fun animationNodePositionElement(node : Node, creator : AnimationNodePosition.() -> Unit)
    {
        val animationNodePosition = AnimationNodePosition(node)
        creator(animationNodePosition)

        synchronized(this.animations)
        {
            this.animations.add(animationNodePosition)
        }
    }

    @AnimationDSL
    fun animationGroup(creator : AnimationGroup.() -> Unit)
    {
        val animationGroup = AnimationGroup()
        creator(animationGroup)

        synchronized(this.animations)
        {
            this.animations.add(animationGroup)
        }
    }

    @AnimationDSL
    fun animationTask(taskContext : TaskContext = TaskContext.INDEPENDENT, task : () -> Unit)
    {
        synchronized(this.animations)
        {
            this.animations.add(AnimationTask(taskContext, task))
        }
    }

    fun animationTexture(start : GameImage, end : GameImage, transitionMillisecond : Long,
                         gameImageInterpolationType : GameImageInterpolationType = GameImageInterpolationMelt) : Texture
    {
        val animationTexture = AnimationTexture(start, end, transitionMillisecond, gameImageInterpolationType)

        synchronized(this.animations)
        {
            this.animations.add(animationTexture)
        }

        return animationTexture.texture
    }

    fun addAnimation(animation : Animation)
    {
        synchronized(this.animations)
        {
            this.animations.add(animation)
        }
    }

    override fun started()
    {
        synchronized(this.animations)
        {
            this.animationIndex = 0

            if (this.animations.isNotEmpty())
            {
                this.animations[this.animationIndex].start()
            }
        }
    }

    override fun animate(time : Long) : Boolean
    {
        synchronized(this.animations)
        {
            if (this.animationIndex >= this.animations.size)
            {
                return false
            }
        }

        var animation =
            synchronized(this.animations)
            {
                this.animations[this.animationIndex]
            }

        while (true)
        {
            animation.animate()

            if (animation.playing)
            {
                return true
            }

            synchronized(this.animations)
            {
                this.animationIndex ++

                if (this.animationIndex >= this.animations.size)
                {
                    return false
                }

                animation = this.animations[this.animationIndex]
                animation.start()
            }
        }
    }
}
