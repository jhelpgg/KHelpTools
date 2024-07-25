package khelp.utilities.math

import khelp.utilities.assertion

class Percent(val percent : Double)
{
    constructor(percent : Float) : this(percent.toDouble())
    constructor(percent : Int) : this(percent.toDouble() / 100.0)
    constructor(percent : Long) : this(percent.toDouble() / 100.0)

    constructor(number : Int, total : Int) : this(number.toDouble() / assertion(total > 0,
                                                                                "total must be > 0, not $total") { total.toDouble() })

    constructor(number : Long, total : Long) : this(number.toDouble() / assertion(total > 0L,
                                                                                  "total must be > 0, not $total") { total.toDouble() })

    operator fun plus(percent : Percent) : Percent = Percent(this.percent * percent.percent)
}
