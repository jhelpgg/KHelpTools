package khelp.utilities.extensions

import khelp.utilities.regex.RegularExpression
import khelp.utilities.regex.RegularExpressionGroup
import khelp.utilities.text.CharactersInterval
import khelp.utilities.text.SimpleCharactersInterval

val Char.interval: CharactersInterval get() = SimpleCharactersInterval(this, this)

val Char.ignoreCaseInterval: CharactersInterval get() = this.toLowerCase().interval + this.toUpperCase()

operator fun Char.plus(charactersInterval: CharactersInterval): CharactersInterval =
    charactersInterval + this

fun Char.toUnicode(): String
{
    val stringBuilder = StringBuilder(6)
    stringBuilder.append(java.lang.Integer.toHexString(this.toInt()))

    while (stringBuilder.length < 4)
    {
        stringBuilder.insert(0, '0')
    }

    stringBuilder.insert(0, "\\u")
    return stringBuilder.toString()
}

val Char.regularExpression: RegularExpression get() = this.interval.regularExpression

val Char.allCharactersExcludeThis: RegularExpression get() = this.interval.allCharactersExcludeThose

val Char.ignoreCaseRegularExpression: RegularExpression get() = this.ignoreCaseInterval.regularExpression

operator fun Char.plus(regularExpression: RegularExpression) =
    this.regularExpression + regularExpression

operator fun Char.plus(regularExpression: RegularExpressionGroup) =
    this.regularExpression + regularExpression

infix fun Char.OR(regularExpression: RegularExpression) =
    this.regularExpression OR regularExpression

infix fun Char.OR(regularExpression: RegularExpressionGroup) =
    this.regularExpression OR regularExpression

fun Char.zeroOrMore(): RegularExpression =
    this.regularExpression.zeroOrMore()

fun Char.oneOrMore(): RegularExpression =
    this.regularExpression.oneOrMore()

fun Char.zeroOrOne(): RegularExpression =
    this.regularExpression.zeroOrOne()

fun Char.exactTimes(times: Int): RegularExpression =
    this.regularExpression.exactTimes(times)

fun Char.atLeast(times: Int): RegularExpression =
    this.regularExpression.atLeast(times)

fun Char.atMost(times: Int): RegularExpression =
    this.regularExpression.atMost(times)

fun Char.between(minimum: Int, maximum: Int): RegularExpression =
    this.regularExpression.between(minimum, maximum)

