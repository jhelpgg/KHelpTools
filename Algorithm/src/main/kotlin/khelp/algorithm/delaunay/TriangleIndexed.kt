package khelp.algorithm.delaunay

import java.awt.geom.Path2D
import java.util.Objects
import khelp.math.matrix.Matrix
import khelp.utilities.math.area
import khelp.utilities.math.isTrigonometricWay

/**
 * Represents a triangle with indexed points.
 * The points are ordered to make the triangle points description in trigonometric way (counter clock wize)
 *
 * @property point1 the first indexed point of the triangle
 * @property point2 the second indexed point of the triangle
 * @property point3 the third indexed point of the triangle
 */
class TriangleIndexed(val point1 : PointIndexed, point2 : PointIndexed, point3 : PointIndexed)
{
    val point2 : PointIndexed
    val point3 : PointIndexed

    /**
     * Represents a polygon as a Path2D.Float object.
     * The polygon can be used to draw and manipulate shapes.
     */
    val polygon : Path2D.Float

    /**
     * The area of a triangle
     */
    val area : Float = area(this.point1.x, this.point1.y, point2.x, point2.y, point3.x, point3.y)

    /**
     * Matrix used to determine if a point is inside the circumscribed circle of the triangle.
     */
    private val inCircumscribedCircleMatrix : Matrix

    init
    {
        // Order points in trigonometric way
        if (isTrigonometricWay(this.point1.x, this.point1.y, point2.x, point2.y, point3.x, point3.y))
        {
            this.point2 = point2
            this.point3 = point3
        }
        else
        {
            this.point2 = point3
            this.point3 = point2
        }

        // Compute the polygon represents the triangle
        this.polygon = Path2D.Float()
        this.polygon.moveTo(this.point1.x, this.point1.y)
        this.polygon.lineTo(this.point2.x, this.point2.y)
        this.polygon.lineTo(this.point3.x, this.point3.y)
        this.polygon.closePath()

        // Prepare the matrix for determine if a point is inside the circumscribed circle of the triangle.
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

    /**
     * Indicates if a point is inside the triangle
     */
    operator fun contains(point : PointIndexed) : Boolean =
        isTrigonometricWay(point.x, point.y, this.point1.x, this.point1.y, this.point2.x, this.point2.y)
        && isTrigonometricWay(point.x, point.y, this.point2.x, this.point2.y, this.point3.x, this.point3.y)
        && isTrigonometricWay(point.x, point.y, this.point3.x, this.point3.y, this.point1.x, this.point1.y)

    fun contains(x : Float, y : Float) : Boolean =
        isTrigonometricWay(x, y, this.point1.x, this.point1.y, this.point2.x, this.point2.y)
        && isTrigonometricWay(x, y, this.point2.x, this.point2.y, this.point3.x, this.point3.y)
        && isTrigonometricWay(x, y, this.point3.x, this.point3.y, this.point1.x, this.point1.y)

    /**
     * Indicates if a point is inside the circumscribed circle of the triangle
     */
    fun inCircumscribedCircle(point : PointIndexed) : Boolean
    {
        /*
            | x1 y1 x1²+y1² 1 |
            | x2 y2 x2²+y2² 1 |
            | x3 y3 x3²+y3² 1 | >= 0
            | px py px²+py² 1 |
         */
        // Set the point to the matrix
        this.inCircumscribedCircleMatrix[0, 3] = point.x.toDouble()
        this.inCircumscribedCircleMatrix[1, 3] = point.y.toDouble()
        this.inCircumscribedCircleMatrix[2, 3] = point.xx.toDouble() + point.yy.toDouble()

        return this.inCircumscribedCircleMatrix.determinant() > 0.0
    }

    /**
     * Compute the coefficients applied to each triangle point to place the point.
     *
     * It can be used as barycenter or interpolation
     *
     * WARNING : Have meaning only if the point is inside the triangle
     *
     * @return Computed coefficients
     */
    fun coefficients(x : Float, y : Float) : Coefficients
    {
        val areaP23 = area(x, y, this.point2.x, this.point2.y, this.point3.x, this.point3.y)
        val areaP31 = area(x, y, this.point3.x, this.point3.y, this.point1.x, this.point1.y)
        val coefficientOnPoint1 = areaP23 / this.area
        val coefficientOnPoint2 = areaP31 / this.area
        val coefficientOnPoint3 = 1f - coefficientOnPoint1 - coefficientOnPoint2
        return Coefficients(coefficientOnPoint1, coefficientOnPoint2, coefficientOnPoint3)
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