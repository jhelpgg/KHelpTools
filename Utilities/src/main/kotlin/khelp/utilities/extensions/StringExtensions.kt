package khelp.utilities.extensions

import java.util.Base64
import java.util.Locale
import khelp.utilities.argumentCheck
import khelp.utilities.regex.LOCALE_SEPARATOR
import khelp.utilities.regex.RegularExpression
import khelp.utilities.regex.RegularExpressionGroup
import khelp.utilities.regex.WHITE_SPACE
import khelp.utilities.regex.WORD_EXTEND
import khelp.utilities.text.EmptyCharactersInterval
import khelp.utilities.text.SimpleCharactersInterval
import khelp.utilities.text.UnionCharactersInterval

val String.utf8 get() = this.toByteArray(Charsets.UTF_8)

val String.base64
    get() = Base64.getDecoder()
        .decode(this)

val String.regularExpression: RegularExpression get() = RegularExpression.text(this)

val String.ignoreCaseRegularExpression: RegularExpression
    get()
    {
        if (this.isEmpty())
        {
            return this.regularExpression
        }

        val characters = this.toCharArray()
        var regularExpression = characters[0].ignoreCaseRegularExpression

        for (index in 1 until characters.size)
        {
            regularExpression += characters[index].ignoreCaseRegularExpression
        }

        return regularExpression
    }

/**
 * Transform to regular expression that match any String composed of extended word characters except this String.
 *
 * The excluded String must be it self composed of extended word characters
 *
 * @throws IllegalArgumentException If String contains at least one non word extended character or empty
 */
val String.anyWordExceptThis: RegularExpression
    get()
    {
        argumentCheck(WORD_EXTEND.matches(this)) { "The given String '$this' contains some illegal characters not consider as 'word' character" }
        val interval = this.toCharArray().interval
        val format =
            when (interval)
            {
                is EmptyCharactersInterval  -> throw IllegalArgumentException("String is empty")
                is SimpleCharactersInterval -> interval.format("[", "]", "[", "]", "-", true)
                is UnionCharactersInterval  ->
                {
                    val stringBuilder = StringBuilder()
                    stringBuilder.append('[')

                    for (simpleInterval in interval.simpleIntervals)
                    {
                        stringBuilder.append(simpleInterval.minimum.toUnicode())

                        if (simpleInterval.minimum < simpleInterval.maximum)
                        {
                            stringBuilder.append('-')
                            stringBuilder.append(simpleInterval.maximum.toUnicode())
                        }
                    }

                    stringBuilder.append(']')
                    stringBuilder.toString()
                }
            }

        return RegularExpression("\\w+(?<!\\W\\Q$this\\E)(?<!^\\Q$this\\E)(?!$format)")
    }

/**

 * Transform to regular expression that match any String composed of non white space characters except this String.
 *
 * The excluded String must be it self composed of non white space characters
 *
 * @throws IllegalArgumentException If String contains at least one white space character or empty
 */
val String.anyNonWhiteSpaceExceptThis: RegularExpression
    get()
    {
        argumentCheck(!WHITE_SPACE.matcher(this)
            .find()) { "The given String '$this' contains at least on white space" }
        val interval = this.toCharArray().interval
        val format =
            when (interval)
            {
                is EmptyCharactersInterval  -> throw IllegalArgumentException("String is empty")
                is SimpleCharactersInterval -> interval.format("[", "]", "[", "]", "-", true)
                is UnionCharactersInterval  ->
                {
                    val stringBuilder = StringBuilder()
                    stringBuilder.append('[')

                    for (simpleInterval in interval.simpleIntervals)
                    {
                        stringBuilder.append(simpleInterval.minimum.toUnicode())

                        if (simpleInterval.minimum < simpleInterval.maximum)
                        {
                            stringBuilder.append('-')
                            stringBuilder.append(simpleInterval.maximum.toUnicode())
                        }
                    }

                    stringBuilder.append(']')
                    stringBuilder.toString()
                }
            }

        return RegularExpression("\\S+(?<!\\s\\Q$this\\E)(?<!^\\Q$this\\E)(?!$format)")
    }

infix fun String.OR(regularExpression: RegularExpression) =
    this.regularExpression OR regularExpression

infix fun String.OR(regularExpression: RegularExpressionGroup) =
    this.regularExpression OR regularExpression

fun String.zeroOrMore(): RegularExpression =
    this.regularExpression.zeroOrMore()

fun String.oneOrMore(): RegularExpression =
    this.regularExpression.oneOrMore()

fun String.zeroOrOne(): RegularExpression =
    this.regularExpression.zeroOrOne()

fun String.exactTimes(times: Int): RegularExpression =
    this.regularExpression.exactTimes(times)

fun String.atLeast(times: Int): RegularExpression =
    this.regularExpression.atLeast(times)

fun String.atMost(times: Int): RegularExpression =
    this.regularExpression.atMost(times)

fun String.between(minimum: Int, maximum: Int): RegularExpression =
    this.regularExpression.between(minimum, maximum)

fun String.toLocale(): Locale
{
    val split = LOCALE_SEPARATOR.split(this, 3)

    return when (split.size)
    {
        1    -> Locale(split[0])
        2    -> Locale(split[0], split[1])
        else -> Locale(split[0], split[1], split[2])
    }
}
