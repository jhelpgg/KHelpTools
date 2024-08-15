package khelp.engine3d.animation

import khelp.engine3d.extensions.position
import khelp.engine3d.render.AnimationDSL
import khelp.engine3d.render.Node
import khelp.utilities.collections.SortedArray
import kotlin.math.max

class AnimationNodePosition(private val node : Node) : Animation()
{
    private val elements = SortedArray<AnimationNodePositionElement>(unique = true)
    private lateinit var startPosition : NodePosition

    @AnimationDSL
    fun add(timeMilliseconds : Long, interpolation : Interpolation = LinearInterpolation,
            positionCreator : NodePositionCreator.() -> Unit)
    {
        val time = max(0L, timeMilliseconds)
        var index =
            this.elements.indexOfFirst { animationNodePositionElement -> animationNodePositionElement.time > time }

        if (index < 0)
        {
            index = this.elements.size - 1
        }
        else
        {
            index --
        }

        val nodePositionCreator =
            if (index >= 0)
            {
                NodePositionCreator(this.elements[index].nodePosition)
            }
            else
            {
                NodePositionCreator(this.node)
            }

        positionCreator(nodePositionCreator)
        this.elements.removeIf { animationNodePositionElement -> animationNodePositionElement.time == time }
        this.elements += AnimationNodePositionElement(time, nodePositionCreator.position, interpolation)
    }

    override fun started()
    {
        this.startPosition = this.node.position
    }

    override fun animate(time : Long) : Boolean
    {
        val index =
            this.elements.indexOfFirst { animationNodePositionElement -> animationNodePositionElement.time >= time }

        if (index < 0)
        {
            this.node.position(this.elements[this.elements.size - 1].nodePosition)
            return false
        }

        val after = this.elements[index]

        if (after.time == time)
        {
            this.node.position(after.nodePosition)
            return true
        }

        val before =
            if (index == 0)
            {
                AnimationNodePositionElement(0L, this.startPosition, LinearInterpolation)
            }
            else
            {
                this.elements[index - 1]
            }

        val end = after.interpolation((time - before.time).toDouble() / (after.time - before.time).toDouble())
        val start = 1.0 - end
        val positionStart = before.nodePosition
        val positionEnd = after.nodePosition

        this.node.x = (positionStart.x * start + positionEnd.x * end).toFloat()
        this.node.y = (positionStart.y * start + positionEnd.y * end).toFloat()
        this.node.z = (positionStart.z * start + positionEnd.z * end).toFloat()

        this.node.angleX = (positionStart.angleX * start + positionEnd.angleX * end).toFloat()
        this.node.angleY = (positionStart.angleY * start + positionEnd.angleY * end).toFloat()
        this.node.angleZ = (positionStart.angleZ * start + positionEnd.angleZ * end).toFloat()

        this.node.scaleX = (positionStart.scaleX * start + positionEnd.scaleX * end).toFloat()
        this.node.scaleY = (positionStart.scaleY * start + positionEnd.scaleY * end).toFloat()
        this.node.scaleZ = (positionStart.scaleZ * start + positionEnd.scaleZ * end).toFloat()

        return true
    }
}
