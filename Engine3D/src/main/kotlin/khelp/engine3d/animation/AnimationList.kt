package khelp.engine3d.animation

import khelp.engine3d.render.AnimationDSL
import khelp.engine3d.render.Node

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
