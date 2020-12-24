package khelp.utilities.math

import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.exp

/**
 * Double precision, the "zero"
 */
val EPSILON = maxOf(Double.MIN_VALUE,
                    abs(Math.E - exp(1.0)),
                    abs(Math.PI - acos(-1.0)))

/**
 * Float precision, the "zero"
 */
val EPSILON_FLOAT = maxOf(Float.MIN_VALUE,
                          abs(Math.E.toFloat() - exp(1.0).toFloat()),
                          abs(Math.PI.toFloat() - acos(-1.0).toFloat()))

/**
 * Indicates if given value can be considered as zero
 *
 * @param value Value to test
 * @return `true` if given value can be considered as zero
 */
fun isNul(value: Double) = Math.abs(value) <= EPSILON

/**
 * Indicates if given value can be considered as zero
 *
 * @param value Value to test
 * @return `true` if given value can be considered as zero
 */
fun isNul(value: Float) = Math.abs(value) <= EPSILON_FLOAT


/**
 * Indicates if two given real can be considered as equals
 *
 * @param value1 First real
 * @param value2 Second real
 * @return `true` if two given real can be considered as equals
 */
fun equals(value1: Double, value2: Double) = isNul(value1 - value2)

/**
 * Indicates if two given real can be considered as equals
 *
 * @param value1 First real
 * @param value2 Second real
 * @return `true` if two given real can be considered as equals
 */
fun equals(value1: Float, value2: Float) = isNul(value1 - value2)

fun min(char1: Char, char2: Char): Char =
    if (char1 <= char2)
    {
        char1
    }
    else
    {
        char2
    }

fun max(char1: Char, char2: Char): Char =
    if (char1 >= char2)
    {
        char1
    }
    else
    {
        char2
    }

fun sign(int: Int): Int =
    when
    {
        int < 0  -> -1
        int == 0 -> 0
        else     -> 1
    }


fun sign(long: Long): Int =
    when
    {
        long < 0L  -> -1
        long == 0L -> 0
        else       -> 1
    }

fun sign(float: Float): Int =
    when
    {
        isNul(float) -> 0
        float < 0f   -> -1
        else         -> 1
    }


fun sign(double: Double): Int =
    when
    {
        isNul(double) -> 0
        double < 0.0  -> -1
        else          -> 1
    }