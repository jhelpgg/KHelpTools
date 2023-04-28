package khelp.math.extensions

import khelp.math.Rational
import khelp.math.formal.Division
import khelp.math.formal.Function
import khelp.math.formal.Subtraction
import khelp.math.formal.constant

/**
 * Transform this number to rational
 */
fun Number.toRational() =
    when (this)
    {
        is Rational                        -> this
        is Byte, is Short, is Int, is Long -> Rational.createRational(this.toLong())
        is Float, is Double                -> Rational.createRational(this.toDouble())
        else                               -> Rational.INVALID
    }

/**
 * Add this number with given rational
 */
operator fun Number.plus(rational: Rational) = this.toRational() + rational

/**
 * Subtract this number with given rational
 */
operator fun Number.minus(rational: Rational) = this.toRational() - rational

/**
 * Multiply this number with given rational
 */
operator fun Number.times(rational: Rational) = this.toRational() * rational

/**
 * Divide this number with given rational
 */
operator fun Number.div(rational: Rational) = this.toRational() / rational

/**
 * Compare this number with given rational
 */
operator fun Number.compareTo(rational: Rational) =
    this.toRational()
        .compareTo(rational)


/**
 * Transform this number to a function
 */
fun Number.toFunction() : Function = constant(this.toDouble())

/**
 * Add this number to a function
 * @param function Function to add
 * @return Addition result
 */
operator fun Number.plus(function : Function) = Function.createAddition(this.toFunction(), function)

/**
 * Subtract this number to a function
 * @param function Function to subtract
 * @return Subtraction result
 */
operator fun Number.minus(function : Function) = Subtraction(this.toFunction(), function)

/**
 * Multiply this number to a function
 * @param function Function to multiply
 * @return Multiplication result
 */
operator fun Number.times(function : Function) = Function.createMultiplication(this.toFunction(), function)

/**
 * Divide this number to a function
 * @param function Function to divide
 * @return Division result
 */
operator fun Number.div(function : Function) = Division(this.toFunction(), function)
