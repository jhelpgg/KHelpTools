package khelp.utilities.extensions

import khelp.utilities.regex.RegularExpression
import khelp.utilities.regex.RegularExpressionGroup
import khelp.utilities.text.CharactersInterval
import khelp.utilities.text.EmptyCharactersInterval

/**
 * String representation with customizable header, separator and footer
 */
fun CharArray.string(header: String = "[", separator: String = ", ", footer: String = "]"): String
{
    val stringBuilder = StringBuilder()
    stringBuilder.append(header)

    if (this.isNotEmpty())
    {
        stringBuilder.append(this[0])

        for (index in 1 until this.size)
        {
            stringBuilder.append(separator)
            stringBuilder.append(this[index])
        }
    }

    stringBuilder.append(footer)
    return stringBuilder.toString()
}

fun CharArray.same(other: CharArray): Boolean
{
    val size = this.size

    if (size != other.size)
    {
        return false
    }

    for (index in 0 until size)
    {
        if (this[index] != other[index])
        {
            return false
        }
    }

    return true
}

val CharArray.interval: CharactersInterval
    get()
    {
        val size = this.size

        if (size == 0)
        {
            return EmptyCharactersInterval
        }

        var interval = this[0].interval

        for (index in 1 until size)
        {
            interval += this[index]
        }

        return interval
    }

val CharArray.ignoreCaseInterval: CharactersInterval
    get()
    {
        val size = this.size

        if (size == 0)
        {
            return EmptyCharactersInterval
        }

        var interval = this[0].ignoreCaseInterval

        for (index in 1 until size)
        {
            interval += this[index].ignoreCaseInterval
        }

        return interval
    }

val CharArray.regularExpression: RegularExpression get() = this.interval.regularExpression

val CharArray.allCharactersExcludeThose: RegularExpression get() = this.interval.allCharactersExcludeThose

val CharArray.ignoreCaseRegularExpression: RegularExpression get() = this.ignoreCaseInterval.regularExpression

operator fun CharArray.plus(regularExpression: RegularExpression) =
    this.regularExpression + regularExpression

operator fun CharArray.plus(regularExpression: RegularExpressionGroup) =
    this.regularExpression + regularExpression

infix fun CharArray.OR(regularExpression: RegularExpression) =
    this.regularExpression OR regularExpression

infix fun CharArray.OR(regularExpression: RegularExpressionGroup) =
    this.regularExpression OR regularExpression

fun CharArray.zeroOrMore(): RegularExpression =
    this.regularExpression.zeroOrMore()

fun CharArray.oneOrMore(): RegularExpression =
    this.regularExpression.oneOrMore()

fun CharArray.zeroOrOne(): RegularExpression =
    this.regularExpression.zeroOrOne()

fun CharArray.exactTimes(times: Int): RegularExpression =
    this.regularExpression.exactTimes(times)

fun CharArray.atLeast(times: Int): RegularExpression =
    this.regularExpression.atLeast(times)

fun CharArray.atMost(times: Int): RegularExpression =
    this.regularExpression.atMost(times)

fun CharArray.between(minimum: Int, maximum: Int): RegularExpression =
    this.regularExpression.between(minimum, maximum)
