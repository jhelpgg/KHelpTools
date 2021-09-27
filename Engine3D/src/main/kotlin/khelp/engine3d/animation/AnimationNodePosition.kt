package khelp.engine3d.animation

import khelp.engine3d.extensions.position
import khelp.engine3d.render.AnimationDSL
import khelp.engine3d.render.Node
import khelp.utilities.collections.sortedArray
import kotlin.math.max

class AnimationNodePosition(private val node : Node) : Animation()
{
    private val elements = sortedArray<AnimationNodePositionElement>(unique = true)
    private lateinit var startPosition : NodePosition

    @AnimationDSL
    fun add(timeMilliseconds : Long, positionCreator : NodePositionCreator.() -> Unit)
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
        this.elements += AnimationNodePositionElement(time, nodePositionCreator.position)
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
                AnimationNodePositionElement(0L, this.startPosition)
            }
            else
            {
                this.elements[index - 1]
            }

        val laps = after.time - before.time
        val start = after.time - time
        val positionStart = before.nodePosition
        val end = time - before.time
        val positionEnd = after.nodePosition

        this.node.x = (positionStart.x * start + positionEnd.x * end) / laps
        this.node.y = (positionStart.y * start + positionEnd.y * end) / laps
        this.node.z = (positionStart.z * start + positionEnd.z * end) / laps

        this.node.angleX = (positionStart.angleX * start + positionEnd.angleX * end) / laps
        this.node.angleY = (positionStart.angleY * start + positionEnd.angleY * end) / laps
        this.node.angleZ = (positionStart.angleZ * start + positionEnd.angleZ * end) / laps

        this.node.scaleX = (positionStart.scaleX * start + positionEnd.scaleX * end) / laps
        this.node.scaleY = (positionStart.scaleY * start + positionEnd.scaleY * end) / laps
        this.node.scaleZ = (positionStart.scaleZ * start + positionEnd.scaleZ * end) / laps

        return true
    }
}
