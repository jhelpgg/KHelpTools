package khelp.engine3d.animation

import khelp.utilities.math.sign

internal class AnimationNodePositionElement(val time : Long,
                                            val nodePosition : NodePosition) : Comparable<AnimationNodePositionElement>
{
    override fun compareTo(other : AnimationNodePositionElement) : Int =
        sign(this.time - other.time)

}
