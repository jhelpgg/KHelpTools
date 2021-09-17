package khelp.utilities.extensions

import khelp.utilities.argumentCheck
import khelp.utilities.regex.LOCALE_SEPARATOR
import khelp.utilities.regex.RegularExpression
import khelp.utilities.regex.RegularExpressionGroup
import khelp.utilities.regex.WHITE_SPACE
import khelp.utilities.regex.WORD_EXTEND
import khelp.utilities.text.EmptyCharactersInterval
import khelp.utilities.text.SimpleCharactersInterval
import khelp.utilities.text.UnionCharactersInterval
import java.util.Base64
import java.util.Locale

val String.utf8 get() = this.toByteArray(Charsets.UTF_8)

val String.base64
    get() = Base64.getDecoder()
        .decode(this)

val String.regularExpression : RegularExpression get() = RegularExpression.text(this)

val String.ignoreCaseRegularExpression : RegularExpression
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
val String.anyWordExceptThis : RegularExpression
    get()
    {
        argumentCheck(WORD_EXTEND.matches(
            this)) { "The given String '$this' contains some illegal characters not consider as 'word' character" }
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
val String.anyNonWhiteSpaceExceptThis : RegularExpression
    get()
    {
        argumentCheck(! WHITE_SPACE.matcher(this)
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

/**
 * Create regular expression that match to this string or given regular expression
 */
infix fun String.OR(regularExpression : RegularExpression) =
    this.regularExpression OR regularExpression

/**
 * Create regular expression that match to this string or given group
 */
infix fun String.OR(regularExpression : RegularExpressionGroup) =
    this.regularExpression OR regularExpression

/**
 * Crete regular expression that match zero, one or several times this string
 */
fun String.zeroOrMore() : RegularExpression =
    this.regularExpression.zeroOrMore()

/**
 * Crete regular expression that match at least one time this string
 */
fun String.oneOrMore() : RegularExpression =
    this.regularExpression.oneOrMore()

/**
 * Crete regular expression that match zero or one time this string
 */
fun String.zeroOrOne() : RegularExpression =
    this.regularExpression.zeroOrOne()

/**
 * Crete regular expression that match exactly a number of times this string
 */
fun String.exactTimes(times : Int) : RegularExpression =
    this.regularExpression.exactTimes(times)

/**
 * Crete regular expression that match at least a number of times this string
 */
fun String.atLeast(times : Int) : RegularExpression =
    this.regularExpression.atLeast(times)

/**
 * Crete regular expression that match at most a number of times this string
 */
fun String.atMost(times : Int) : RegularExpression =
    this.regularExpression.atMost(times)

/**
 * Crete regular expression that match this string a number of times between a minimum and a maximum
 */
fun String.between(minimum : Int, maximum : Int) : RegularExpression =
    this.regularExpression.between(minimum, maximum)

/**
 * Try to parse this string to a locale
 */
fun String.toLocale() : Locale
{
    val split = LOCALE_SEPARATOR.split(this, 3)

    return when (split.size)
    {
        1    -> Locale(split[0])
        2    -> Locale(split[0], split[1])
        else -> Locale(split[0], split[1], split[2])
    }
}

/**
 * If the string size is less or equals to given number, it returns as is.
 *
 * Else, the result will be, first part of the string, follow by `...` and end part of the string. The result will have the exact requested size.
 *
 * For exemples
 *
 *      +-----------------------------------------------------+-----------------+
 *      |                         Call                        |      Result     |
 *      +-----------------------------------------------------+-----------------+
 *      | "The end of the world!".ellipseIfMoreThan(9)        | "The...ld!"     |
 *      | "The beginning of the start.".ellipseIfMoreThan(12) | "The b...art."  |
 *      | "Touch to much".ellipseIfMoreThan(23)               | "Touch to much" |
 *      +-----------------------------------------------------+-----------------+
 *
 */
fun String.ellipseIfMoreThan(maximumNumberCharacters : Int) : String
{
    argumentCheck(
        maximumNumberCharacters >= 5) { "The maximum number if characters must be >=5, not $maximumNumberCharacters" }
    val size = this.length

    if (size <= maximumNumberCharacters)
    {
        return this
    }

    val rightSize = (maximumNumberCharacters - 3) / 2
    val leftSize = maximumNumberCharacters - 3 - rightSize
    return "${this.substring(0, leftSize)}...${this.substring(size - rightSize)}"
}

/**
 * If the string size is less or equals to given number, it returns as is.
 *
 * Else, the result will be, first part of the string, follow by `...`. The result will have the exact requested size.
 *
 * For exemples
 *
 *      +---------------------------------------------+-----------------+
 *      |                     Call                    |      Result     |
 *      +---------------------------------------------+-----------------+
 *      | "The end of the world!".onlyFirst(9)        | "The en..."     |
 *      | "The beginning of the start.".onlyFirst(12) | "The begin..."  |
 *      | "Touch to much".onlyFirst(23)               | "Touch to much" |
 *      +---------------------------------------------+-----------------+
 *
 */
fun String.onlyFirst(maximumNumberCharacters : Int) : String
{
    argumentCheck(
        maximumNumberCharacters >= 5) { "The maximum number if characters must be >=5, not $maximumNumberCharacters" }
    val size = this.length

    if (size <= maximumNumberCharacters)
    {
        return this
    }

    return "${this.substring(0, maximumNumberCharacters - 3)}..."
}

/**
 * If the string size is less or equals to given number, it returns as is.
 *
 * Else, the result will be, `...` follow by last part of the string. The result will have the exact requested size.
 *
 * For exemples
 *
 *      +--------------------------------------------+-----------------+
 *      |                    Call                    |      Result     |
 *      +--------------------------------------------+-----------------+
 *      | "The end of the world!".onlyLast(9)        | "...world!"     |
 *      | "The beginning of the start.".onlyLast(12) | "...he start."  |
 *      | "Touch to much".onlyLast(23)               | "Touch to much" |
 *      +--------------------------------------------+-----------------+
 *
 */
fun String.onlyLast(maximumNumberCharacters : Int) : String
{
    argumentCheck(
        maximumNumberCharacters >= 5) { "The maximum number if characters must be >=5, not $maximumNumberCharacters" }
    val size = this.length

    if (size <= maximumNumberCharacters)
    {
        return this
    }

    return "...${this.substring(size - (maximumNumberCharacters - 3))}"
}

fun String.int(defaultValue : Int = 0) =
    try
    {
        this.trim()
            .toInt()
    }
    catch (_ : Exception)
    {
        defaultValue
    }


fun String.long(defaultValue : Long = 0L) =
    try
    {
        this.trim()
            .toLong()
    }
    catch (_ : Exception)
    {
        defaultValue
    }

fun String.float(defaultValue : Float = 0f) =
    try
    {
        this.trim()
            .toFloat()
    }
    catch (_ : Exception)
    {
        defaultValue
    }

fun String.double(defaultValue : Double = 0.0) =
    try
    {
        this.trim()
            .toDouble()
    }
    catch (_ : Exception)
    {
        defaultValue
    }

val String.removeWhiteCharacters : String
    get()
    {
        val stringBuilder = StringBuilder()

        for (character in this)
        {
            if (character > ' ')
            {
                stringBuilder.append(character)
            }
        }

        return stringBuilder.toString()
    }

fun String.compareToIgnoreCaseFirst(string : String) : Int
{
    val comparision = this.compareTo(string, true)

    if (comparision != 0)
    {
        return comparision
    }

    return this.compareTo(string)
}