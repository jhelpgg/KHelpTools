package khelp.engine3d.utils.delaunay

import java.awt.geom.Path2D
import java.util.Objects
import khelp.math.matrix.Matrix

class TriangleIndexed(val point1 : PointIndexed, point2 : PointIndexed, point3 : PointIndexed)
{
    val point2 : PointIndexed
    val point3 : PointIndexed
    val polygon : Path2D.Float
    private val inCircumscribedCircleMatrix : Matrix

    init
    {
        val vx12 = point2.x - this.point1.x
        val vy12 = point2.y - this.point1.y
        val vx13 = point3.x - this.point1.x
        val vy13 = point3.y - this.point1.y
        /*
            | vx12 vx13 |
            | vy12 vy13 | = (vx12 * vy13) - (vy12*vx13)
         */
        val determinant = (vx12 * vy13) - (vy12 * vx13)

        if (determinant >= 0.0)
        {
            this.point2 = point2
            this.point3 = point3
        }
        else
        {
            this.point2 = point3
            this.point3 = point2
        }

        this.polygon = Path2D.Float()
        this.polygon.moveTo(this.point1.x, this.point1.y)
        this.polygon.lineTo(this.point2.x, this.point2.y)
        this.polygon.lineTo(this.point3.x, this.point3.y)
        this.polygon.closePath()

        val matrix = Matrix(4, 4)

        matrix[0, 0] = this.point1.x.toDouble()
        matrix[1, 0] = this.point1.y.toDouble()
        matrix[2, 0] = this.point1.xx.toDouble() + this.point1.yy.toDouble()
        matrix[3, 0] = 1.0

        matrix[0, 1] = this.point2.x.toDouble()
        matrix[1, 1] = this.point2.y.toDouble()
        matrix[2, 1] = this.point2.xx.toDouble() + this.point2.yy.toDouble()
        matrix[3, 1] = 1.0

        matrix[0, 2] = this.point3.x.toDouble()
        matrix[1, 2] = this.point3.y.toDouble()
        matrix[2, 2] = this.point3.xx.toDouble() + this.point3.yy.toDouble()
        matrix[3, 2] = 1.0

        // Point add later
        matrix[3, 3] = 1.0

        this.inCircumscribedCircleMatrix = matrix
    }

    operator fun contains(point : PointIndexed) : Boolean =
        this.polygon.contains(point.x.toDouble(), point.y.toDouble())

    fun inCircumscribedCircle(point : PointIndexed) : Boolean
    {
        /*
            | x1 y1 x1²+y1² 1 |
            | x2 y2 x2²+y2² 1 |
            | x3 y3 x3²+y3² 1 | >= 0
            | px py px²+py² 1 |

         */
        this.inCircumscribedCircleMatrix[0, 3] = point.x.toDouble()
        this.inCircumscribedCircleMatrix[1, 3] = point.y.toDouble()
        this.inCircumscribedCircleMatrix[2, 3] = point.xx.toDouble() + point.yy.toDouble()

        return this.inCircumscribedCircleMatrix.determinant() > 0.0
    }

    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other                             -> true
            null == other || other !is TriangleIndexed -> false
            else                                       ->
                (this.point1 == other.point1 && this.point2 == other.point2 && this.point3 == other.point3) ||
                (this.point1 == other.point1 && this.point2 == other.point3 && this.point3 == other.point2) ||
                (this.point1 == other.point2 && this.point2 == other.point1 && this.point3 == other.point3) ||
                (this.point1 == other.point2 && this.point2 == other.point3 && this.point3 == other.point1) ||
                (this.point1 == other.point3 && this.point2 == other.point1 && this.point3 == other.point2) ||
                (this.point1 == other.point3 && this.point2 == other.point2 && this.point3 == other.point1)
        }

    override fun hashCode() : Int =
        Objects.hash(this.point1, this.point2, this.point3)

    override fun toString() : String =
        "{${this.point1} | ${this.point2} | ${this.point3}}"
}