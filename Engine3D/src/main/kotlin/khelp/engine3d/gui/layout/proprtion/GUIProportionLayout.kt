package khelp.engine3d.gui.layout.proprtion

import khelp.engine3d.gui.component.GUIComponent
import khelp.engine3d.gui.layout.GUILayout
import java.awt.Dimension
import kotlin.math.max

class GUIProportionLayout : GUILayout<GUIProportionConstraint>()
{
    override fun layout(parentWidth : Int, parentHeight : Int,
                        components : List<Pair<GUIComponent, GUIProportionConstraint>>)
    {
        for ((component, constraint) in components)
        {
            component.x = (parentWidth * constraint.x.percent).toInt()
            component.y = (parentHeight * constraint.y.percent).toInt()
            component.width = (parentWidth * constraint.width.percent).toInt()
            component.height = (parentHeight * constraint.height.percent).toInt()
        }
    }

    override fun preferredSize(components : List<Pair<GUIComponent, GUIProportionConstraint>>) : Dimension
    {
        var width = 0
        var height = 0

        for ((component, constraint) in components)
        {
            val preferred = component.preferredSize()
            width = max(width,
                        (preferred.width + ((constraint.x.percent * preferred.width) / constraint.width.percent)).toInt())
            height = max(height,
                         (preferred.height + ((constraint.y.percent * preferred.height) / constraint.height.percent)).toInt())
        }

        return Dimension(width, height)
    }
}
