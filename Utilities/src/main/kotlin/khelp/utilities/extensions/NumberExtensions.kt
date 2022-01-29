package khelp.utilities.extensions

import khelp.utilities.math.Percent

operator fun Number.plus(percent : Percent) : Double = this.toDouble() * (1.0 + percent.percent)
operator fun Number.minus(percent : Percent) : Double = this.toDouble() * (1.0 - percent.percent)