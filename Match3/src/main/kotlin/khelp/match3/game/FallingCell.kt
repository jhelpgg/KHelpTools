package khelp.match3.game

import khelp.match3.model.Cell
import java.util.Objects

class FallingCell(val xCell : Int, val yCell : Int, val cell : Cell) : Comparable<FallingCell>
{
    var deltaY : Int = GameScreen.CELL_SIZE

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return false
        }

        if (null == other || other !is FallingCell)
        {
            return false
        }

        return this.xCell == other.xCell && this.yCell == other.yCell
    }

    override fun hashCode() : Int = Objects.hash(this.xCell, this.yCell)

    override fun compareTo(other : FallingCell) : Int
    {
        val comparison = this.yCell - other.yCell

        if (comparison != 0)
        {
            return comparison
        }

        return this.xCell - other.xCell
    }
}


