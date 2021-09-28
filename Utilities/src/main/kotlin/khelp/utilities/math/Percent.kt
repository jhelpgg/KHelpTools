package khelp.utilities.math

import khelp.utilities.extensions.bounds

class Percent(percent : Double)
{
    constructor(percent : Float) : this(percent.toDouble())
    constructor(percent : Int) : this(percent.toDouble() / 100.0)
    constructor(percent : Long) : this(percent / 100.0)

    constructor(number : Int, total : Int) : this(number.toDouble() / total.toDouble())
    constructor(number : Long, total : Long) : this(number.toDouble() / total.toDouble())

    val percent = percent.bounds(0.0, 1.0)
}
