package khelp.engine3d.render.complex.robot

import khelp.engine3d.animation.Animation
import khelp.engine3d.animation.Interpolation
import khelp.engine3d.animation.LinearInterpolation
import khelp.engine3d.render.AnimationDSL
import khelp.utilities.collections.sortedArray
import kotlin.math.max

class RobotAnimation(private val robot : Robot) : Animation()
{
    private val elements = sortedArray<RobotAnimationElement>(unique = true)
    private lateinit var startPosition : RobotPosition

    @AnimationDSL
    fun add(timeMilliseconds : Long, interpolation : Interpolation = LinearInterpolation,
            positionCreator : RobotPositionCreator.() -> Unit)
    {
        val time = max(0L, timeMilliseconds)
        var index =
            this.elements.indexOfFirst { robotAnimationElement -> robotAnimationElement.time > time }

        if (index < 0)
        {
            index = this.elements.size - 1
        }
        else
        {
            index --
        }

        val robotPositionCreator =
            if (index >= 0)
            {
                RobotPositionCreator(this.elements[index].robotPosition)
            }
            else
            {
                RobotPositionCreator(this.robot)
            }

        positionCreator(robotPositionCreator)
        this.elements.removeIf { robotAnimationElement -> robotAnimationElement.time == time }
        this.elements += RobotAnimationElement(time, robotPositionCreator.robotPosition, interpolation)
    }

    override fun started()
    {
        this.startPosition = this.robot.robotPosition
    }

    override fun animate(time : Long) : Boolean
    {
        val index =
            this.elements.indexOfFirst { robotAnimationElement -> robotAnimationElement.time >= time }

        if (index < 0)
        {
            this.robot.robotPosition = this.elements[this.elements.size - 1].robotPosition
            return false
        }

        val after = this.elements[index]

        if (after.time == time)
        {
            this.robot.robotPosition = after.robotPosition
            return true
        }

        val before =
            if (index == 0)
            {
                RobotAnimationElement(0L, this.startPosition, LinearInterpolation)
            }
            else
            {
                this.elements[index - 1]
            }

        val end = after.interpolation((time - before.time).toDouble() / (after.time - before.time).toDouble())
        val start = 1.0 - end
        val positionStart = before.robotPosition
        val positionEnd = after.robotPosition
        this.robot.robotPosition =
            RobotPosition(
                (positionStart.neckAngleX * start + positionEnd.neckAngleX).toFloat(),
                (positionStart.neckAngleY * start + positionEnd.neckAngleY).toFloat(),
                (positionStart.neckAngleZ * start + positionEnd.neckAngleZ).toFloat(),

                (positionStart.rightShoulderAngleX * start + positionEnd.rightShoulderAngleX).toFloat(),
                (positionStart.rightShoulderAngleZ * start + positionEnd.rightShoulderAngleZ).toFloat(),

                (positionStart.rightElbowAngleX * start + positionEnd.rightElbowAngleX).toFloat(),

                (positionStart.leftShoulderAngleX * start + positionEnd.leftShoulderAngleX).toFloat(),
                (positionStart.leftShoulderAngleZ * start + positionEnd.leftShoulderAngleZ).toFloat(),

                (positionStart.leftElbowAngleX * start + positionEnd.leftElbowAngleX).toFloat(),

                (positionStart.rightAssAngleX * start + positionEnd.rightAssAngleX).toFloat(),
                (positionStart.rightAssAngleZ * start + positionEnd.rightAssAngleZ).toFloat(),

                (positionStart.rightKneeAngleX * start + positionEnd.rightKneeAngleX).toFloat(),

                (positionStart.leftAssAngleX * start + positionEnd.leftAssAngleX).toFloat(),
                (positionStart.leftAssAngleZ * start + positionEnd.leftAssAngleZ).toFloat(),

                (positionStart.leftKneeAngleX * start + positionEnd.leftKneeAngleX).toFloat()
            )
        return true
    }
}
