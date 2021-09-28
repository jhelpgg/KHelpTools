package khelp.engine3d.utils

import khelp.engine3d.geometry.Point2D
import khelp.math.matrix.Matrix
import khelp.utilities.math.equals
import khelp.utilities.math.sign
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow


/**
 * 2! / (index! * (2 - index)!)
 * index=0 => 2 / (1 * 2) = 1
 * index=1 => 2 / (1 * 1) = 2
 * index=2 => 2 / (2 * 1) = 1
 */
private val combination2 = floatArrayOf(1f, 2f, 1f)


/**
 * 3! / (index! * (3 - index)!)
 * index=0 => 6 / (1 * 6) = 1
 * index=1 => 6 / (1 * 2) = 3
 * index=2 => 6 / (2 * 1) = 3
 * index=3 => 6 / (6 * 1) = 1
 */
private val combination3 = floatArrayOf(1f, 3f, 3f, 1f)

private fun Bernoulli2(index : Int, t : Float) =
    combination2[index] * t.pow(index.toFloat()) * (1f - t).pow((2 - index).toFloat())

private fun Bernoulli3(index : Int, t : Float) =
    combination3[index] * t.pow(index.toFloat()) * (1f - t).pow((3 - index).toFloat())

/**
 * Compute cubic invoke at a given time
 *
 * @param cp Current value
 * @param p1 First control value
 * @param p2 Second control value
 * @param p3 End value
 * @param t  Interpolation time
 * @return Interpolated value
 */
private fun cubic(cp : Float, p1 : Float, p2 : Float, p3 : Float, t : Float) =
    Bernoulli3(0, t) * cp +
    Bernoulli3(1, t) * p1 +
    Bernoulli3(2, t) * p2 +
    Bernoulli3(3, t) * p3

/**
 * 2! / (index! * (2 - index)!)
 * index=0 => 2 / (1 * 2) = 1
 * index=1 => 2 / (1 * 1) = 2
 * index=2 => 2 / (2 * 1) = 1
 */
private val combinationDouble2 = doubleArrayOf(1.0, 2.0, 1.0)


/**
 * 3! / (index! * (3 - index)!)
 * index=0 => 6 / (1 * 6) = 1
 * index=1 => 6 / (1 * 2) = 3
 * index=2 => 6 / (2 * 1) = 3
 * index=3 => 6 / (6 * 1) = 1
 */
private val combinationDouble3 = doubleArrayOf(1.0, 3.0, 3.0, 1.0)

private fun BernoulliDouble2(index : Int, t : Double) =
    combinationDouble2[index] * t.pow(index.toDouble()) * (1f - t).pow((2 - index).toDouble())

private fun BernoulliDouble3(index : Int, t : Double) =
    combinationDouble3[index] * t.pow(index.toDouble()) * (1f - t).pow((3 - index).toDouble())

/**
 * Compute cubic invoke at a given time
 *
 * @param cp Current value
 * @param p1 First control value
 * @param p2 Second control value
 * @param p3 End value
 * @param t  Interpolation time
 * @return Interpolated value
 */
fun cubic(cp : Double, p1 : Double, p2 : Double, p3 : Double, t : Double) =
    BernoulliDouble3(0, t) * cp +
    BernoulliDouble3(1, t) * p1 +
    BernoulliDouble3(2, t) * p2 +
    BernoulliDouble3(3, t) * p3

fun quadratic(cp : Double, p1 : Double, p2 : Double, t : Double) =
    BernoulliDouble2(0, t) * cp +
    BernoulliDouble2(1, t) * p1 +
    BernoulliDouble2(2, t) * p2

/**
 * Compute interpolated values cubic for a given precision
 *
 * @param cp        Current value
 * @param p1        First control value
 * @param p2        Second control value
 * @param p3        End value
 * @param precision Precision used
 * @return Interpolated values
 */
fun cubics(cp : Float, p1 : Float, p2 : Float, p3 : Float, precision : Int) : FloatArray
{
    val cubics = FloatArray(precision)
    val step = 1f / (precision - 1f)
    var actual = 0f

    for (i in 0 until precision)
    {
        if (i == precision - 1)
        {
            actual = 1f
        }

        cubics[i] = cubic(cp, p1, p2, p3, actual)
        actual += step
    }

    return cubics
}

/**
 * Compute quadratic invoke at a given time
 *
 * @param cp Current value
 * @param p1 Control value
 * @param p2 End value
 * @param t  Interpolation time
 * @return Interpolated value
 */
private fun quadratic(cp : Float, p1 : Float, p2 : Float, t : Float) =
    Bernoulli2(0, t) * cp +
    Bernoulli2(1, t) * p1 +
    Bernoulli2(2, t) * p2

/**
 * Compute interpolated values quadratic for a given precision
 *
 * @param cp        Current value
 * @param p1        Control value
 * @param p2        End value
 * @param precision Precision used
 * @return Interpolated values
 */
fun quadratics(cp : Float, p1 : Float, p2 : Float, precision : Int) : FloatArray
{
    val quadratics = FloatArray(precision)
    val step = 1f / (precision - 1f)
    var actual = 0f

    for (i in 0 until precision)
    {
        if (i == precision - 1)
        {
            actual = 1f
        }

        quadratics[i] = quadratic(cp, p1, p2, actual)
        actual += step
    }

    return quadratics
}

/*
p1 = s11 + t1 * (s12-s11) (0<=t1<=1)
p2 = s21 + t2 * (s22-s21) (0<=t2<=1)
p1 = p2 =>
     s11x + t1 * (s12x-s11x) = s21x + t2 * (s22x-s21x)
     s11y + t1 * (s12y-s11y) = s21y + t2 * (s22y-s21y)

     t1 * (s12x-s11x) - t2 * (s22x-s21x) = s21x - s11x
     t1 * (s12y-s11y) - t2 * (s22y-s21y) = s21y - s11y

     [s12x-s11x   s21x-s22x] * [t1] = [s21x-s11x]
     [s12y-s11y   s21y-s22y]   [t2]   [s21y-s11y]

                                   -1
     [t1] = [s12x-s11x   s21x-s22x]  *  [s21x-s11x]
     [t2]   [s12y-s11y   s21y-s22y]     [s21y-s11y]

*/

fun segmentIntersection(segment1x1 : Float, segment1y1 : Float, segment1x2 : Float, segment1y2 : Float,
                        segment2x1 : Float, segment2y1 : Float, segment2x2 : Float, segment2y2 : Float) : Point2D?
{
    val matrix = Matrix(2, 2)
    matrix[0, 0] = (segment1x2 - segment1x1).toDouble()
    matrix[1, 0] = (segment2x1 - segment2x2).toDouble()
    matrix[0, 1] = (segment1y2 - segment1y1).toDouble()
    matrix[1, 1] = (segment2y1 - segment2y2).toDouble()

    val result = AtomicReference<Point2D>(null)

    if (! Matrix.isNul(matrix.determinant()))
    {
        val future =
            matrix
                .invert()
                .and { invert ->
                    val m00 = invert[0, 0].toFloat()
                    val m10 = invert[1, 0].toFloat()
                    val m01 = invert[0, 1].toFloat()
                    val m11 = invert[1, 1].toFloat()
                    val factor1 = m00 * (segment2x1 - segment1x1) + m10 * (segment2y1 - segment1y1)
                    val factor2 = m01 * (segment2x1 - segment1x1) + m11 * (segment2y1 - segment1y1)

                    if ((factor1 in 0f .. 1f) && (factor2 in 0f .. 1f))
                    {
                        result.set(Point2D(segment1x1 + factor1 * (segment1x2 - segment1x1),
                                           segment1y1 + factor1 * (segment1y2 - segment1y1)))
                    }
                }

        future.waitCompletion()
    }

    return result.get()
}

/*
 p = s1 + t * (s2-s1)  | (0<=t<=1)
 =>
    px = s1x + t * (s2x - s1x)
    py = s1y + t * (s2y - s1y)

    t' = (px - s1x) / (s2x - s1x)
    t" = (py - s1y) / (s2y - s1y)
 */
fun segmentContainsPoint(segmentX1 : Float, segmentY1 : Float, segmentX2 : Float, segmentY2 : Float,
                         pointX : Float, pointY : Float) : Boolean
{
    if (equals(segmentX1, segmentX2))
    {
        return equals(pointX, segmentX1) && pointY >= min(segmentY1, segmentY2) && pointY <= max(segmentY1, segmentY2)
    }

    if (equals(segmentY1, segmentY2))
    {
        return equals(pointY, segmentY1) && pointX >= min(segmentX1, segmentX2) && pointX <= max(segmentX1, segmentX2)
    }

    val tPrime = (pointX - segmentX1) / (segmentX2 - segmentX1)
    val tSecond = (pointY - segmentY1) / (segmentY2 - segmentY1)
    return equals(tPrime, tSecond) && tPrime >= 0f && tPrime <= 1f
}

fun counterClockWise(point1 : Point2D, point2 : Point2D, point3 : Point2D) : Boolean
{
    val x31 = point1.x - point3.x
    val x32 = point2.x - point3.x
    val y31 = point1.y - point3.y
    val y32 = point2.y - point3.y
    return sign(x31 * y32 - y31 * x32) >= 0
}

fun convex(points : List<Point2D>) : Boolean = convex(*points.toTypedArray())

fun convex(vararg points : Point2D) : Boolean
{
    val size = points.size

    if (size < 4)
    {
        return true
    }

    val counterClockWise = counterClockWise(points[0], points[1], points[2])

    for (index in 3 until size)
    {
        if (counterClockWise != counterClockWise(points[index - 2], points[index - 1], points[index]))
        {
            return false
        }
    }

    return counterClockWise == counterClockWise(points[size - 2], points[size - 1], points[0]) &&
           counterClockWise == counterClockWise(points[size - 1], points[0], points[1])
}
