package khelp.engine3d.gui.layout.vertical

import khelp.engine3d.gui.component.GUIComponent
import khelp.engine3d.gui.layout.GUILayout
import java.awt.Dimension
import kotlin.math.max

class GUIVerticalLayout : GUILayout<GUIVerticalConstraint>()
{
    var margin : Int = 4
        set(value)
        {
            field = max(0, value)
        }

    override fun layout(parentWidth : Int, parentHeight : Int,
                        components : List<Pair<GUIComponent, GUIVerticalConstraint>>)
    {
        var y = 0

        for ((component, constraint) in components)
        {
            val size = component.preferredSize()

            component.x =
                when (constraint)
                {
                    GUIVerticalConstraint.LEFT   -> 0
                    GUIVerticalConstraint.CENTER -> (parentWidth - size.width) / 2
                    GUIVerticalConstraint.RIGHT  -> parentWidth - size.width
                }

            component.y = y
            component.width = size.width
            component.height = size.height

            y += size.height + this.margin

            if (y >= parentHeight)
            {
                return
            }
        }
    }

    override fun preferredSize(components : List<Pair<GUIComponent, GUIVerticalConstraint>>) : Dimension
    {
        if (components.isEmpty())
        {
            return Dimension(16, 16 + this.margin * 2)
        }

        var size = components[0].first.preferredSize()
        var width = size.width
        var height = size.height

        for (index in 1 until components.size)
        {
            size = components[index].first.preferredSize()
            width = max(width, size.width)
            height += this.margin + size.height
        }

        return Dimension(width, height)
    }
}
