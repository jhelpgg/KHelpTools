package khelp.algorithm.interpolation

import khelp.image.JHelpImage
import khelp.thread.flow.FlowData

abstract class InterpolationImage(val width : Int, val height : Int)
{
    val image = JHelpImage(this.width, this.height)

    private val updateSource = FlowData<Unit>()
    val updateFlow = this.updateSource.flow

    protected fun update()
    {
        this.updateSource.publish(Unit)
    }
}