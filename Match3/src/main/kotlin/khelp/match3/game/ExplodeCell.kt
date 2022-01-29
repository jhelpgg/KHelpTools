package khelp.match3.game

import khelp.match3.model.Cell
import khelp.match3.model.Explosion
import java.util.Objects

class ExplodeCell(val cellX : Int, val cellY : Int, val replacedBy : Cell) : Comparable<ExplodeCell>
{
    val explosion = Explosion()

    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other                         -> true
            null == other || other !is ExplodeCell -> false
            else                                   -> this.cellX == other.cellX && this.cellY == other.cellY
        }

    override fun hashCode() : Int = Objects.hash(this.cellX, this.cellY)

    override fun compareTo(other : ExplodeCell) : Int
    {
        val comparison = this.cellY - other.cellY

        if (comparison != 0)
        {
            return comparison
        }

        return this.cellX - other.cellX
    }
}
