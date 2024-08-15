package khelp.utilities.interval.dsl

import khelp.utilities.interval.Interval
import khelp.utilities.interval.IntervalEmpty
import khelp.utilities.interval.IntervalSimple
import khelp.utilities.interval.limit.Infinite
import khelp.utilities.interval.limit.Limit
import khelp.utilities.interval.limit.LimitValue
import khelp.utilities.interval.union

@IntervalDsl
class IntervalCreator<C : Comparable<C>>
{
    private var interval : Interval<C> = IntervalEmpty<C>()

    @IntervalDsl
    val C.include : Limit<C> get() = LimitValue<C>(this, true)
    @IntervalDsl
    val C.exclude : Limit<C> get() = LimitValue<C>(this, false)
    @IntervalDsl
    val infinite : Limit<C> get() = Infinite<C>()

    @IntervalDsl
    operator fun Limit<C>.rangeTo(limit : Limit<C>)
    {
        if(this is LimitValue<C> && limit is LimitValue<C>)
        {
            val comparison = this.value.compareTo(limit.value)

            when
            {
                comparison > 0 -> return
                comparison == 0 && (this.include.not() || limit.include.not()) -> return
            }
        }

        this@IntervalCreator.interval =
            this@IntervalCreator.interval union IntervalSimple<C>(this, limit)
    }

    operator fun invoke() : Interval<C> = this.interval
}