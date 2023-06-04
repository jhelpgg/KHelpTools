package khelp.engine3d.gui.layout.grid

import khelp.engine3d.gui.component.GUIComponent
import khelp.engine3d.gui.layout.GUILayout
import java.awt.Dimension
import kotlin.math.max
import kotlin.math.min

class GUIGridLayout(numberColumn : Int) : GUILayout<GUIGridConstraint>()
{
    var marginHorizontal = 1
        set(value)
        {
            field = max(0, value)
        }
    var marginVertical = 1
        set(value)
        {
            field = max(0, value)
        }
    private val numberColumn = max(1, numberColumn)

    fun add(component : GUIComponent)
    {
        this.add(component, GUIGridConstraint)
    }

    override fun layout(parentWidth : Int, parentHeight : Int, components : List<Pair<GUIComponent, GUIGridConstraint>>)
    {
        if (components.isEmpty())
        {
            // No components, nothing to do
            return
        }

        val number = components.count { (component, _) -> component.visible }
        val numberLine = (number + this.numberColumn - 1) / this.numberColumn
        val linesHeights = IntArray(numberLine)
        var totalHeight = 0
        var columnHeight = 0
        var column = 0
        var line = 0

        for ((component, _) in components)
        {
            if (component.visible)
            {
                val height = component.preferredSize().height
                columnHeight = max(columnHeight, height)
                column = (column + 1) % this.numberColumn

                if (column == 0)
                {
                    linesHeights[line] = columnHeight
                    totalHeight += columnHeight
                    columnHeight = 0
                    line ++
                }
            }
        }

        if (column > 0)
        {
            linesHeights[line] = columnHeight
            totalHeight += columnHeight
        }

        val celWidth = (parentWidth - (this.numberColumn + 1) * this.marginHorizontal) / this.numberColumn
        val leftHeight = parentHeight - (numberLine + 1) * this.marginVertical
        var x = this.marginHorizontal
        var y = this.marginVertical
        column = 0
        line = 0
        var height = min(linesHeights[0], (linesHeights[0] * leftHeight) / totalHeight)

        for ((component, _) in components)
        {
            if (component.visible)
            {
                component.x = x
                component.y = y
                component.width = celWidth
                component.height = height

                x += celWidth + this.marginHorizontal
                column = (column + 1) % this.numberColumn

                if (column == 0)
                {
                    x = this.marginHorizontal
                    y += height + this.marginVertical
                    line ++
                    height = min(linesHeights[line], (linesHeights[line] * leftHeight) / totalHeight)
                }
            }
        }
    }

    override fun preferredSize(components : List<Pair<GUIComponent, GUIGridConstraint>>) : Dimension
    {
        var width = 1
        var height = 1
        var column = 0
        var line = 1
        var columnHeight = 1

        for ((component, _) in components)
        {
            if (component.visible)
            {
                val size = component.preferredSize()
                width = max(width, size.width)
                columnHeight = max(columnHeight, size.height)

                column = (column + 1) % this.numberColumn

                if (column == 0)
                {
                    height = max(height, columnHeight)
                    columnHeight = 1
                    line ++
                }
            }
        }

        height = max(height, columnHeight)
        return Dimension(this.numberColumn * (width + this.marginHorizontal) + this.marginHorizontal,
                         line * (height + this.marginVertical) + this.marginVertical)
    }
}
