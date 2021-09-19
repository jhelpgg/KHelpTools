package khelp.engine3d.utils

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
    Bernoulli2( 0, t) * cp +
    Bernoulli2( 1, t) * p1 +
    Bernoulli2( 2, t) * p2

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
