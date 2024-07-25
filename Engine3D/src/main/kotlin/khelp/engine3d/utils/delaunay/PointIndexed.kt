package khelp.engine3d.utils.delaunay

class PointIndexed(val index : Int, val x : Float, val y : Float)
{
    val xx = this.x * this.x
    val yy = this.y * this.y

    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other                          -> true
            null == other || other !is PointIndexed -> false
            else                                    -> this.index == other.index
        }

    override fun hashCode() : Int =
        this.index

    override fun toString() : String =
        "${this.index}:(${this.x}, ${this.y})"
}
