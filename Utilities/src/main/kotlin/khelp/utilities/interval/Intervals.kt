package khelp.utilities.interval

import khelp.utilities.interval.dsl.IntervalCreator
import khelp.utilities.interval.dsl.IntervalDsl
import khelp.utilities.interval.limit.Limit
import khelp.utilities.interval.limit.LimitValue

/**
 * Creates an interval using a DSL syntax.
 *
 * Example :
 *
 * ```kotlin
 * import khelp.utilities.interval.interval
 *
 * // ....
 *
 *        val interval =
 *             interval<Int> {
 *                 infinite..(-42).include
 *                 (-5).include..(-3).include
 *                 2.include..12.exclude
 *                 42.include..73.include
 *                 85.include..85.include
 *                 99.exclude..infinite
 *             }
 * ```
 *
 * @param create the DSL block to configure the interval
 * @return the created interval
 */
@IntervalDsl
fun <C : Comparable<C>> interval(create : IntervalCreator<C>.() -> Unit) : Interval<C>
{
    val intervalCreator = IntervalCreator<C>()
    intervalCreator.create()
    return intervalCreator()
}

/**
 * Computes the union of this interval with another interval.
 *
 * @param other the interval to compute the union with
 * @return the interval representing the union of this interval and the other interval
 */
infix fun <C : Comparable<C>> Interval<C>.union(other : Interval<C>) : Interval<C> =
    IntervalUnion.union(this, other)

/**
 * Returns true if the interval is empty, false otherwise.
 *
 * @return true if the interval is empty, false otherwise.
 */
val Interval<*>.empty : Boolean get() = this is IntervalEmpty<*>

/**
 * Determines if the interval is not empty.
 * An interval is considered not empty if it is not an instance of [IntervalEmpty].
 *
 * @return true if the interval is not empty, false otherwise.
 */
val Interval<*>.notEmpty : Boolean get() = this !is IntervalEmpty<*>

/**
 * Intersects two intervals and returns the resulting intersection interval.
 *
 * @param other the interval to intersect with.
 * @return the intersection of the two intervals.
 */
infix fun <C : Comparable<C>> Interval<C>.intersect(other : Interval<C>) : Interval<C> =
    when (this)
    {
        is IntervalEmpty<C>  -> IntervalEmpty<C>()
        is IntervalSimple<C> -> intersect(this, other)
        is IntervalUnion<C>  -> intersect(this, other)
    }

/**
 * Calculates the intersection of simple interval and any interval.
 *
 * @param intervalSimple the first interval to intersect.
 * @param intervalToIntersect the second interval to intersect.
 * @return the intersection of the two intervals.
 */
private fun <C : Comparable<C>> intersect(intervalSimple : IntervalSimple<C>,
                                          intervalToIntersect : Interval<C>) : Interval<C> =
    when (intervalToIntersect)
    {
        is IntervalEmpty<C>  -> IntervalEmpty<C>()
        is IntervalSimple<C> -> intersect(intervalSimple, intervalToIntersect)
        is IntervalUnion<C>  -> intersect(intervalSimple, intervalToIntersect)
    }

/**
 * Calculates the intersection of two simple intervals.
 *
 * @param intervalSimple1 the first interval to intersect.
 * @param intervalSimple2 the second interval to intersect.
 * @return the intersection of the two intervals.
 */
private fun <C : Comparable<C>> intersect(intervalSimple1 : IntervalSimple<C>,
                                          intervalSimple2 : IntervalSimple<C>) : Interval<C>
{
    val minimum : Limit<C> =
        if (intervalSimple1.minimum is LimitValue<C>)
        {
            if (intervalSimple2.minimum is LimitValue<C>)
            {
                val comparison = intervalSimple1.minimum.value.compareTo(intervalSimple2.minimum.value)

                when
                {
                    comparison < 0 ->
                        intervalSimple2.minimum

                    comparison > 0 ->
                        intervalSimple1.minimum

                    else           ->
                        LimitValue<C>(intervalSimple1.minimum.value,
                                      intervalSimple1.minimum.include && intervalSimple2.minimum.include)
                }
            }
            else
            {
                intervalSimple1.minimum
            }
        }
        else
        {
            intervalSimple2.minimum
        }

    val maximum : Limit<C> =
        if (intervalSimple1.maximum is LimitValue<C>)
        {
            if (intervalSimple2.maximum is LimitValue<C>)
            {
                val comparison = intervalSimple1.maximum.value.compareTo(intervalSimple2.maximum.value)

                when
                {
                    comparison > 0 ->
                        intervalSimple2.maximum

                    comparison < 0 ->
                        intervalSimple1.maximum

                    else           ->
                        LimitValue<C>(intervalSimple1.maximum.value,
                                      intervalSimple1.maximum.include && intervalSimple2.maximum.include)
                }
            }
            else
            {
                intervalSimple1.maximum
            }
        }
        else
        {
            intervalSimple2.maximum
        }

    if (minimum !is LimitValue<C> || maximum !is LimitValue<C>)
    {
        return IntervalSimple(minimum, maximum)
    }

    val comparison = minimum.value.compareTo(maximum.value)

    return when
    {
        comparison < 0                     -> IntervalSimple(minimum, maximum)
        comparison > 0                     -> IntervalEmpty<C>()
        minimum.include && maximum.include -> IntervalSimple<C>(minimum, maximum)
        else                               -> IntervalEmpty<C>()
    }
}

/**
 * Calculates the intersection of an interval simple and an interval union.
 *
 * @param intervalSimple the simple interval
 * @param intervalUnion the union of intervals
 * @return the intersection of the interval simple and interval union
 */
private fun <C : Comparable<C>> intersect(intervalSimple : IntervalSimple<C>,
                                          intervalUnion : IntervalUnion<C>) : Interval<C>
{
    var result : Interval<C> = IntervalEmpty<C>()

    for (interval in intervalUnion.intervals)
    {
        result = result union intersect(intervalSimple, interval)
    }

    return result
}

/**
 * Calculates the intersection of an interval union and an interval.
 *
 * @param intervalUnion the interval union to intersect.
 * @param intervalToIntersect the interval to intersect.
 * @return the intersection of the interval union and the interval.
 */
private fun <C : Comparable<C>> intersect(intervalUnion : IntervalUnion<C>,
                                          intervalToIntersect : Interval<C>) : Interval<C>
{
    var result : Interval<C> = IntervalEmpty<C>()

    for (interval in intervalUnion.intervals)
    {
        result = result union intersect(interval, intervalToIntersect)
    }

    return result
}