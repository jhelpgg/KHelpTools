package khelp.engine3d.animation

import khelp.engine3d.render.AnimationDSL
import khelp.engine3d.render.Node
import khelp.thread.parallel
import khelp.utilities.log.verbose
import java.util.concurrent.atomic.AtomicBoolean

internal object AnimationManager
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