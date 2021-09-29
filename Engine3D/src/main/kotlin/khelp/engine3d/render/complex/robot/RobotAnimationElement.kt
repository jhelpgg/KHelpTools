package khelp.engine3d.render.complex.robot

import khelp.engine3d.animation.Interpolation
import khelp.utilities.math.sign

internal class RobotAnimationElement(val time : Long,
                                     val robotPosition : RobotPosition,
                                     val interpolation : Interpolation) : Comparable<RobotAnimationElement>
{
    override fun compareTo(other : RobotAnimationElement) : Int =
        sign(this.time - other.time)
}
