package khelp.math.extensions

import khelp.math.Rational
import khelp.utilities.math.Percent

fun Percent(rational : Rational) : Percent = Percent(rational.toDouble())

fun Percent.toRational() : Rational = Rational.createRational(this.percent)
