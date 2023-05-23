package khelp.engine3d.gui.layout.absolute

import khelp.engine3d.gui.component.GUIComponent
import khelp.engine3d.gui.layout.GUILayout
import java.awt.Dimension
import kotlin.math.max

class GUIAbsoluteLayout : GUILayout<GUIAbsoluteConstraint>()
{
    override fun layout(parentWidth : Int, parentHeight : Int,
                        components : List<Pair<GUIComponent, GUIAbsoluteConstraint>>)
    {
        for ((component, constraint) in components)
        {
            component.x = constraint.x
            component.y = constraint.y
            component.width = constraint.width
            component.height = constraint.height
        }
    }

    override fun preferredSize(components : List<Pair<GUIComponent, GUIAbsoluteConstraint>>) : Dimension
    {
        var width = 1
        var height = 1

        for ((_, constraint) in components)
        {
            width = max(width, constraint.x + constraint.width)
            height = max(height, constraint.y + constraint.height)
        }

        return Dimension(width, height)
    }
}
