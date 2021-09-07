package khelp.ui.layout.table

import java.util.Objects
import kotlin.math.max

class TableLayoutConstraint(val x : Int, val y : Int, numberCellWidth : Int = 1, numberCellHeight : Int = 1)
{
    val numberCellWidth = max(1, numberCellWidth)
    val numberCellHeight = max(1, numberCellHeight)

    /**
     * Current width
     */
    internal var currentWidth = 0

    /**
     * Current height
     */
    internal var currentHeight = 0

    override fun toString() : String =
        "TableLayoutConstraint (${this.x}, ${this.y}) ${this.numberCellWidth}x${this.numberCellHeight}"

    override fun hashCode() : Int = Objects.hash(this.x, this.y, this.numberCellWidth, this.numberCellHeight)

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is TableLayoutConstraint)
        {
            return false
        }

        return this.x == other.x && this.y == other.y && this.numberCellWidth == other.numberCellWidth && this.numberCellHeight == other.numberCellHeight
    }
}
