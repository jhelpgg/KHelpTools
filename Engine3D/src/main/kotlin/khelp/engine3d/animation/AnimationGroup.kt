package khelp.engine3d.animation

import khelp.engine3d.render.AnimationDSL
import khelp.engine3d.render.Node

class AnimationGroup : Animation()
{
    private val animations = ArrayList<Animation>()

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
    fun animationList(creator:AnimationList.()->Unit)
    {
        val animationList = AnimationList()
        creator(animationList)

        synchronized(this.animations)
        {
            this.animations.add(animationList)
        }
    }

    override fun started()
    {
        synchronized(this.animations)
        {
            for (animation in this.animations)
            {
                animation.start()
            }
        }
    }

    override fun animate(time : Long) : Boolean
    {
        var animated = false

        synchronized(this.animations)
        {
            for (animation in this.animations)
            {
                animated = animation.animate(time) || animated
            }
        }

        return animated
    }
}