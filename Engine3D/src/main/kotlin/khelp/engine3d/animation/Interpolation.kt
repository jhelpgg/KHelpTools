package khelp.engine3d.animation

import fr.jhelp.utilities.random
import khelp.engine3d.utils.cubic
import khelp.engine3d.utils.quadratic
import khelp.utilities.extensions.compare
import khelp.utilities.extensions.nul
import khelp.utilities.math.EPSILON
import khelp.utilities.math.square
import kotlin.math.E
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.expm1
import kotlin.math.ln
import kotlin.math.ln1p
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

sealed class Interpolation
{
    abstract operator fun invoke(percent : Double) : Double
}

object LinearInterpolation : Interpolation()
{
    override operator fun invoke(percent : Double) : Double = percent
}

/**
 * Interpolation with acceleration effect
 * @param factor Acceleration factor
 */
class AccelerationInterpolation(factor : Double = 1.0) : Interpolation()
{
    /**Acceleration factor*/
    private val factor = 2.0 * max(EPSILON, factor)

    /**
     * Interpolate value with acceleration effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) = percent.pow(this.factor)
}

/**
 * Interpolation with anticipation effect.
 *
 * Thai is to say it look goes reverse and then go to the good way, like if it take a run-up
 * @param tension Effect factor
 */
class AnticipateInterpolation(tension : Double = 1.0) : Interpolation()
{
    /**Effect factor*/
    private val tension = max(EPSILON, tension)

    /**
     * Interpolate value with anticipation effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        (this.tension + 1.0) * percent * percent * percent - this.tension * percent * percent
}

/**
 * Interpolation with anticipate and overshoot effect
 *
 * Anticipate : Like if it take a run-up
 *
 * Overshoot : Goes to far and return back
 * @param tension Effect factor
 */
class AnticipateOvershootInterpolation(tension : Double = 1.0) : Interpolation()
{
    /**Effect factor*/
    private val tension = max(EPSILON, tension)

    /**
     * Interpolate value with anticipation and overshoot effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        when
        {
            percent.compare(0.5) < 0 ->
            {
                val value = 2.0 * percent
                0.5 * ((this.tension + 1.0) * value * value * value - this.tension * value * value)
            }
            else                     ->
            {
                val value = 2.0 * percent - 2.0
                0.5 * ((this.tension + 1.0) * value * value * value + this.tension * value * value) + 1.0
            }
        }
}

/**
 * Interpolation that make bounce effect
 */
object BounceInterpolation : Interpolation()
{
    /**
     * Interpolate value with bounce effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        when
        {
            percent.compare(0.31489) < 0 -> 8.0 * square(1.1226 * percent)
            percent.compare(0.65990) < 0 -> 8.0 * square(1.1226 * percent - 0.54719) + 0.7
            percent.compare(0.85908) < 0 -> 8.0 * square(1.1226 * percent - 0.8526) + 0.9
            else                         -> 8.0 * square(1.1226 * percent - 1.0435) + 0.95
        }
}

/**
 * Interpolation that bounce
 * @param numberBounce Number of bounce
 */
class BouncingInterpolation(numberBounce : Int = 2) : Interpolation()
{
    /**Number of bounce*/
    private val numberBounce = max(0, numberBounce)

    /**
     * Interpolate value with bounce effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) : Double
    {
        if (this.numberBounce == 0)
        {
            return square(percent)
        }

        var amplitude = 1.0 / (this.numberBounce + 1)

        if (percent.compare(amplitude) < 0)
        {
            return square(percent / amplitude)
        }

        var free = 1.0 - amplitude * 0.56789
        var minimum = 0.56789
        var percentReal = percent - amplitude
        var left = this.numberBounce - 1

        while (percentReal.compare(amplitude) >= 0 && ! amplitude.nul && ! minimum.nul && ! percentReal.nul && left > 0)
        {
            minimum *= 0.56789
            percentReal -= amplitude
            free -= amplitude
            amplitude = free * 0.56789
            left --
        }

        if (left == 0)
        {
            amplitude = free / 2.0
        }

        val squareRoot = sqrt(minimum)
        percentReal = (percentReal - amplitude / 2.0) * (squareRoot * 2.0 / amplitude)
        return min(square(percentReal) + 1.0 - minimum, 1.0)
    }
}

/**
 * Interpolation follow cosinus function
 */
object CosinusInterpolation : Interpolation()
{
    /**
     * Interpolate value with following equation :
     *
     *    1 + cos((t + 1) * PI)
     *    ---------------------
     *              2
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        (1.0 + cos((percent + 1.0) * PI)) / 2.0
}

/**
 * Cubic interpolation
 * @param firstControl First control point
 * @param secondControl Second control point
 */
class CubicInterpolation(private val firstControl : Double = 0.1,
                         private val secondControl : Double = 0.9) : Interpolation()
{
    /**
     * Compute cubic interpolation
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        cubic(0.0, this.firstControl, this.secondControl, 1.0, percent)
}

/**
 * Interpolation with deceleration effect
 * @param factor Deceleration factor
 */
class DecelerationInterpolation(factor : Double = 1.0) : Interpolation()
{
    /**Deceleration factor*/
    private val factor = 2.0 * max(EPSILON, factor)

    /**
     * Interpolate value with deceleration effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        1.0 - (1.0 - percent).pow(this.factor)
}

/**
 * Interpolation follow exponential progression
 */
object ExponentialInterpolation : Interpolation()
{
    /**
     * Interpolate value with following equation :
     *
     *     t
     *    e - 1
     *    ------
     *    e - 1
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        expm1(percent) / (E - 1.0)
}

/**
 * Interpolation with hesitation effect
 */
object HesitateInterpolation : Interpolation()
{
    /**
     * Interpolate value with hesitation effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) : Double
    {
        val value = 2.0 * percent - 1.0
        return 0.5 * (value * value * value + 1.0)
    }
}

/**
 * Interpolation follow logarithm progression
 */
object LogarithmInterpolation : Interpolation()
{
    /**
     * Interpolate value with following equation:
     *
     *    ln(t + 1)
     *    --------
     *     ln(2)
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        ln1p(percent) / ln(2.0)
}

/**
 * Interpolation that overshoot.
 *
 * That is to say it goes to far and then go back to the good place
 * @param tension Effect factor
 */
class OvershootInterpolation(tension : Double = 1.0) : Interpolation()
{
    /**Effect factor*/
    private val tension = max(EPSILON, tension)

    /**
     * Interpolate value with overshoot effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) : Double
    {
        val value = percent - 1.0
        return (this.tension + 1.0) * value * value * value + this.tension * value * value + 1.0
    }
}

/**
 * Quadratic interpolation
 * @param control Control point
 */
class QuadraticInterpolation(private val control : Double = 0.25) : Interpolation()
{
    /**
     * Compute quadratic interpolation
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        quadratic(0.0, this.control, 1.0, percent)
}

/**
 * Interpolation with random progression
 */
object RandomInterpolation : Interpolation()
{
    /**
     * Interpolate value with random progression
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        when
        {
            percent.nul || khelp.utilities.math.equals(percent, 1.0) -> percent
            else                                                     -> random(percent, 1.0)
        }
}

/**
 * Interpolation follow sinus function
 */
object SinusInterpolation : Interpolation()
{
    /**
     * Interpolate value with following equation :
     *
     *    1 + sin(t * PI - PI/2)
     *    ----------------------
     *              2
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        (1.0 + sin(percent * PI - PI / 2.0)) / 2.0
}

/**
 * Interpolation follow square progression
 */
object SquareInterpolation : Interpolation()
{
    /**
     * Interpolate value with following equation:
     *
     *    t²
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) = percent * percent
}

/**
 * Interpolation follow square root progression
 */
object SquareRootInterpolation : Interpolation()
{
    /**
     * Interpolate value with following equation:
     *
     *     ___
     *    V t
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) = sqrt(percent)
}
