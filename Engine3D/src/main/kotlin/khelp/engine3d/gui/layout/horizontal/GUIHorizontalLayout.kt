package khelp.engine3d.gui.layout.horizontal

import khelp.engine3d.gui.component.GUIComponent
import khelp.engine3d.gui.layout.GUILayout
import java.awt.Dimension
import kotlin.math.max

class GUIHorizontalLayout : GUILayout<GUIHorizontalConstraint>()
{
    var margin : Int = 4
        set(value)
        {
            field = max(0, value)
        }

    override fun layout(parentWidth : Int, parentHeight : Int,
                        components : List<Pair<GUIComponent, GUIHorizontalConstraint>>)
    {
        var x = 0

        for ((component, constraint) in components)
        {
            val size = component.preferredSize()
            component.x = x

            component.y =
                when (constraint)
                {
                    GUIHorizontalConstraint.TOP    -> 0
                    GUIHorizontalConstraint.CENTER -> (parentHeight - size.height) / 2
                    GUIHorizontalConstraint.BOTTOM -> parentHeight - size.height
                }

            component.width = size.width
            component.height = size.height

            x += size.width + this.margin

            if (x >= parentWidth)
            {
                return
            }
        }
    }

    override fun preferredSize(components : List<Pair<GUIComponent, GUIHorizontalConstraint>>) : Dimension
    {
        if (components.isEmpty())
        {
            return Dimension(16 + this.margin * 2, 16)
        }

        var size = components[0].first.preferredSize()
        var width = size.width
        var height = size.height

        for (index in 1 until components.size)
        {
            size = components[index].first.preferredSize()
            width += this.margin + size.width
            height = max(height, size.height)
        }

        return Dimension(width, height)

    }
}