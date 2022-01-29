package khelp.utilities.text

import khelp.utilities.extensions.int
import khelp.utilities.extensions.regularExpression
import khelp.utilities.regex.DIGIT
import khelp.utilities.regex.LETTER
import kotlin.math.min

/**
 * Default escape characters : \ (see [StringExtractor])
 */
const val DEFAULT_ESCAPE_CHARACTERS = "\\"

/**
 * Default escape separators : [space], [Line return \n], [tabulation \t], [carriage return \r] (see
 * [StringExtractor])
 */
const val DEFAULT_SEPARATORS = " \n\t\r"

/**
 * Default string limiters : " and ' (see [StringExtractor])
 */
const val DEFAULT_STRING_LIMITERS = "\"'"

/**
 * Compute the last index <= of given offset in the char sequence of one of given characters
 *
 * @param charSequence Char sequence where search one character
 * @param offset       Offset maximum for search
 * @param characters   Characters search
 * @return Index of the last character <= given offset found in char sequence that inside in the given list. -1 if
 * the char
 * sequence doesn't contain any of given characters before the given offset
 */
fun lastIndexOf(charSequence : CharSequence, offset : Int = charSequence.length, vararg characters : Char) : Int
{
    val start = min(charSequence.length - 1, offset)
    var character : Char

    for (index in start downTo 0)
    {
        character = charSequence[index]

        for (car in characters)
        {
            if (car == character)
            {
                return index
            }
        }
    }

    return - 1
}

private val HOLE_INDEX_GROUP = DIGIT.oneOrMore()
    .group()
private val HOLE_REGEX = '{'.regularExpression + HOLE_INDEX_GROUP + '}'

fun replaceHoles(textWithHole : String, vararg replacement : String) : String
{
    val stringBuilder = StringBuilder()

    val matcher = HOLE_REGEX.matcher(textWithHole)
    var start = 0

    while (matcher.find())
    {
        stringBuilder.append(textWithHole.substring(start, matcher.start()))
        val index = matcher.group(HOLE_INDEX_GROUP)
            .int()

        if (index < replacement.size)
        {
            stringBuilder.append(replacement[index])
        }
        else
        {
            stringBuilder.append("-{$index}-")
        }

        start = matcher.end()
    }

    stringBuilder.append(textWithHole.substring(start))
    return stringBuilder.toString()
}

private val PLURAL_INDEX_GROUP = DIGIT.oneOrMore()
    .group()
private val NUMBER_LETTERS_TO_REMOVE_GROUP = DIGIT.oneOrMore()
    .group()
private val ADDITIONAL_PLURAL = LETTER.oneOrMore()
    .group()
private val PLURAL_REGEX =
    '{'.regularExpression + PLURAL_INDEX_GROUP + '|' + NUMBER_LETTERS_TO_REMOVE_GROUP + '|' + ADDITIONAL_PLURAL + '}'

fun replacePlurals(textWithPlurals : String, vararg numbers : Int) : String
{
    val stringBuilder = StringBuilder()

    val matcher = PLURAL_REGEX.matcher(textWithPlurals)
    var start = 0

    while (matcher.find())
    {
        stringBuilder.append(textWithPlurals.substring(start, matcher.start()))
        val index = matcher.group(PLURAL_INDEX_GROUP)
            .int()

        if (index < numbers.size)
        {
            val number = numbers[index]

            if (number >= 2)
            {
                val remove = matcher.group(NUMBER_LETTERS_TO_REMOVE_GROUP)
                    .int()

                if (remove > 0)
                {
                    stringBuilder.delete(stringBuilder.length - remove, stringBuilder.length)
                }

                stringBuilder.append(matcher.group(ADDITIONAL_PLURAL))
            }
        }
        else
        {
            stringBuilder.append("-{$index}-")
        }

        start = matcher.end()
    }

    stringBuilder.append(textWithPlurals.substring(start))
    return stringBuilder.toString()
}

fun conjugatePlural(textWithPluralsAndHole : String, vararg numbers : Int) : String
{
    val plurals = replacePlurals(textWithPluralsAndHole, *numbers)
    val words = numbers
        .map { number -> number.toString() }
        .toTypedArray()
    return replaceHoles(plurals, *words)
}