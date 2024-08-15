package khelp.utilities.interval

import java.util.Objects
import khelp.utilities.collections.SortedArray
import khelp.utilities.interval.limit.Infinite
import khelp.utilities.interval.limit.Limit
import khelp.utilities.interval.limit.LimitValue

/**
 * Represents a union of multiple intervals.
 * The intervals can be inclusive or exclusive on their limits.
 *
 * @param intervals the list of intervals to be union
 *
 * @constructor Creates an IntervalUnion instance with the given list of intervals
 */
class IntervalUnion<C : Comparable<C>> private constructor(val intervals : List<IntervalSimple<C>>) : Interval<C>
{
    companion object
    {
        /**
         * Computes the union of multiple intervals.
         *
         * @param intervals the intervals to compute the union of
         * @return the interval representing the union of the input intervals
         */
        fun <C : Comparable<C>> union(vararg intervals : Interval<C>) : Interval<C>
        {
            val intervalsSimple = SortedArray<IntervalSimple<C>>(true)

            for (interval in intervals)
            {
                when (interval)
                {
                    is IntervalSimple<C> ->
                        intervalsSimple += interval

                    is IntervalUnion<C>  ->
                        for (internInterval in interval.intervals)
                        {
                            intervalsSimple += internInterval
                        }

                    else                 -> Unit
                }
            }

            var size = intervalsSimple.size

            if (size == 0)
            {
                return IntervalEmpty<C>()
            }

            var index = 0

            while (size > 1 && index < size - 1)
            {
                val actualInterval = intervalsSimple[index]
                val nextInterval = intervalsSimple[index + 1]

                if (IntervalUnion.insideIntervalLow(actualInterval.minimum, nextInterval)
                    || IntervalUnion.insideIntervalLow(nextInterval.minimum, actualInterval)
                    || IntervalUnion.insideIntervalHigh(actualInterval.maximum, nextInterval)
                    || IntervalUnion.insideIntervalHigh(nextInterval.maximum, actualInterval))
                {
                    val newMinimum = IntervalUnion.miniumUnion(actualInterval.minimum, nextInterval.minimum)
                    val newMaximum = IntervalUnion.maximumUnion(actualInterval.maximum, nextInterval.maximum)
                    intervalsSimple.remove(index)
                    intervalsSimple.remove(index)
                    intervalsSimple += IntervalSimple(newMinimum, newMaximum)
                    size--
                    continue
                }

                index++
            }

            if (size == 1)
            {
                return intervalsSimple[0]
            }

            return IntervalUnion<C>(intervalsSimple.immutableList())
        }

        /**
         * Checks if the given interval limit is inside the interval simple.
         * If the interval limit is a LimitValue, it checks if the value is within the interval simple.
         * If the interval limit is an Infinite, it returns true if the minimum of the interval simple is infinite.
         *
         * @param intervalLimit the limit to check if it is inside the interval simple
         * @param intervalSimple the interval simple to check if the limit is inside it
         * @return true if the limit is inside the interval simple, false otherwise
         */
        private fun <C : Comparable<C>> insideIntervalLow(intervalLimit : Limit<C>,
                                                          intervalSimple : IntervalSimple<C>) : Boolean
        {
            if (intervalLimit is LimitValue<C>)
            {
                return intervalLimit.value in intervalSimple
            }

            return intervalSimple.minimum is Infinite<C>
        }

        /**
         * Checks if the given interval limit is higher than the interval simple.
         * If the interval limit is a LimitValue, it checks if the value is within the interval simple.
         * If the interval limit is an Infinite, it returns true if the maximum of the interval simple is infinite.
         *
         * @param intervalLimit the limit to check
         * @param intervalSimple the interval to compare against
         * @return true if the interval limit is higher than the interval simple, false otherwise
         */
        private fun <C : Comparable<C>> insideIntervalHigh(intervalLimit : Limit<C>,
                                                           intervalSimple : IntervalSimple<C>) : Boolean
        {
            if (intervalLimit is LimitValue<C>)
            {
                return intervalLimit.value in intervalSimple
            }

            return intervalSimple.maximum is Infinite<C>
        }

        /**
         * Finds the minimum limit between two intervals.
         *
         * @param intervalLimit1 the first limit to compare
         * @param intervalLimit2 the second limit to compare
         * @return the minimum limit between the two input limits
         */
        private fun <C : Comparable<C>> miniumUnion(intervalLimit1 : Limit<C>,
                                                    intervalLimit2 : Limit<C>) : Limit<C>
        {
            if (intervalLimit1 !is LimitValue<C> || intervalLimit2 !is LimitValue<C>)
            {
                return Infinite<C>()
            }

            val comparison = intervalLimit1.value.compareTo(intervalLimit2.value)

            return when
            {
                comparison < 0 -> intervalLimit1
                comparison > 0 -> intervalLimit2
                else           -> LimitValue(intervalLimit1.value, intervalLimit1.include || intervalLimit2.include)
            }
        }

        /**
         * Computes the maximum limit between two interval limits.
         *
         * @param intervalLimit1 the first interval limit
         * @param intervalLimit2 the second interval limit
         * @return the maximum limit between the two interval limits
         */
        private fun <C : Comparable<C>> maximumUnion(intervalLimit1 : Limit<C>,
                                                     intervalLimit2 : Limit<C>) : Limit<C>
        {
            if (intervalLimit1 !is LimitValue<C> || intervalLimit2 !is LimitValue<C>)
            {
                return Infinite<C>()
            }

            val comparison = intervalLimit1.value.compareTo(intervalLimit2.value)

            return when
            {
                comparison > 0 -> intervalLimit1
                comparison < 0 -> intervalLimit2
                else           -> LimitValue(intervalLimit1.value, intervalLimit1.include || intervalLimit2.include)
            }
        }
    }

    override fun contains(value : C) : Boolean =
        this.intervals.any { interval -> value in interval }

    override fun toString() : String =
        this.intervals.joinToString(" U ")

    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other                              -> true
            null == other || other !is IntervalUnion<*> -> false
            else                                        ->
            {
                if (this.intervals.size != other.intervals.size)
                {
                    false
                }
                else
                {
                    var response = true

                    for ((index, value) in this.intervals.withIndex())
                    {
                        if (other.intervals[index] != value)
                        {
                            response = false
                            break
                        }
                    }

                    response
                }
            }
        }

    override fun hashCode() : Int =
        Objects.hash(*this.intervals.toTypedArray())
}