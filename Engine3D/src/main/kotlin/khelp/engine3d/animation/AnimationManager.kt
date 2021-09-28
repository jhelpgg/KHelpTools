package khelp.engine3d.animation

import khelp.engine3d.render.AnimationDSL
import khelp.engine3d.render.Node
import khelp.engine3d.render.Texture
import khelp.thread.TaskContext
import khelp.thread.parallel
import khelp.ui.game.GameImage
import khelp.ui.game.interpolation.GameImageInterpolationMelt
import khelp.ui.game.interpolation.GameImageInterpolationType
import java.util.concurrent.atomic.AtomicBoolean

object AnimationManager
{
    private val animations = HashMap<String, Animation>()
    private val animationPlaying = AtomicBoolean(false)
    private val animationLaunched = AtomicBoolean(false)

    @AnimationDSL
    fun animationNodePositionElement(name : String, node : Node, creator : AnimationNodePosition.() -> Unit)
    {
        val animationNodePosition = AnimationNodePosition(node)
        creator(animationNodePosition)

        synchronized(this.animations)
        {
            this.animations[name] = animationNodePosition
        }
    }

    @AnimationDSL
    fun animationGroup(name : String, creator : AnimationGroup.() -> Unit)
    {
        val animationGroup = AnimationGroup()
        creator(animationGroup)

        synchronized(this.animations)
        {
            this.animations[name] = animationGroup
        }
    }

    @AnimationDSL
    fun animationList(name : String, creator : AnimationList.() -> Unit)
    {
        val animationList = AnimationList()
        creator(animationList)

        synchronized(this.animations)
        {
            this.animations[name] = animationList
        }
    }

    @AnimationDSL
    fun animationTask(name : String, taskContext : TaskContext = TaskContext.INDEPENDENT, task : () -> Unit)
    {
        synchronized(this.animations)
        {
            this.animations[name] = AnimationTask(taskContext, task)
        }
    }

    fun animationTexture(name : String, start : GameImage, end : GameImage, transitionMillisecond : Long,
                         gameImageInterpolationType : GameImageInterpolationType = GameImageInterpolationMelt) : Texture
    {
        val animationTexture = AnimationTexture(start, end, transitionMillisecond, gameImageInterpolationType)

        synchronized(this.animations)
        {
            this.animations[name] = animationTexture
        }

        return animationTexture.texture
    }

    fun addAnimation(name : String, animation : Animation)
    {
        synchronized(this.animations)
        {
            this.animations[name] = animation
        }
    }

    fun play(animationName : String)
    {
        val animation = this.animations[animationName] ?: return

        if (animation.playing)
        {
            return
        }

        animation.start()

        if (this.animationPlaying.compareAndSet(false, true))
        {
            parallel { this.playAnimations() }
        }
        else
        {
            this.animationLaunched.set(true)
        }
    }

    private fun playAnimations()
    {
        var atLeastOneAnimationPlaying : Boolean

        do
        {
            atLeastOneAnimationPlaying = false

            synchronized(this.animations) {
                for (animation in this.animations.values)
                {
                    if (animation.playing)
                    {
                        animation.animate()
                        atLeastOneAnimationPlaying = true
                    }
                }
            }

            Thread.sleep(32)
            atLeastOneAnimationPlaying = atLeastOneAnimationPlaying || this.animationLaunched.getAndSet(false)
        }
        while (atLeastOneAnimationPlaying)

        this.animationPlaying.set(false)
    }
}