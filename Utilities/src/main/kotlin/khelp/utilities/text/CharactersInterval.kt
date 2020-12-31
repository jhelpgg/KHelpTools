package khelp.utilities.text

import khelp.utilities.extensions.plus
import khelp.utilities.extensions.toUnicode

/**
 * Generic interval of characters
 */
sealed class CharactersInterval
{
    abstract operator fun contains(char: Char): Boolean

    abstract operator fun contains(charactersInterval: CharactersInterval): Boolean
}

/**
 * Empty interval of characters
 */
object EmptyCharactersInterval : CharactersInterval()
{
    override fun toString(): String = "[]"

    override operator fun contains(char: Char): Boolean = false

    override operator fun contains(charactersInterval: CharactersInterval): Boolean =
        charactersInterval == EmptyCharactersInterval
}

/**
 * Simple character interval.
 *
 * Characters are between a minimum and a maximum
 */
data class SimpleCharactersInterval internal constructor(val minimum: Char, val maximum: Char) : CharactersInterval(),
                                                                                                 Comparable<SimpleCharactersInterval>
{
    override operator fun compareTo(other: SimpleCharactersInterval): Int
    {
        val comparison = this.minimum - other.minimum

        if (comparison != 0)
        {
            return comparison
        }

        return other.maximum - this.maximum
    }

    override fun toString(): String =
        this.format()

    /**
     * Create a string representation of the interval.
     *
     * It is possible to customise header and footer different for case if only one character or several.
     *
     * Possible to customize the separator between minimum and maximum if more than one character.
     *
     * Possible to print characters in `\uHHHH` version
     *
     * @param uniqueOpenSymbol Header to use if only one character
     * @param uniqueCloseSymbol Footer to use if only one character
     * @param openSymbol Header to use if more than one character
     * @param closeSymbol Footer to use if more than one character
     * @param separatorSymbol Separator between minimum and maximum if more than one character
     * @param unicode Indicates if show `\uHHHH` version or ordinary one
     */
    fun format(uniqueOpenSymbol: String = "{",
               uniqueCloseSymbol: String = "}",
               openSymbol: String = "[",
               closeSymbol: String = "]",
               separatorSymbol: String = ", ",
               unicode: Boolean = false): String =
        if (unicode)
        {
            if (this.minimum == this.maximum)
            {
                "${uniqueOpenSymbol}${this.minimum.toUnicode()}${uniqueCloseSymbol}"
            }
            else
            {
                "${openSymbol}${this.minimum.toUnicode()}${separatorSymbol}${this.maximum.toUnicode()}${closeSymbol}"
            }
        }
        else
        {
            if (this.minimum == this.maximum)
            {
                "${uniqueOpenSymbol}${this.minimum}${uniqueCloseSymbol}"
            }
            else
            {
                "${openSymbol}${this.minimum}${separatorSymbol}${this.maximum}${closeSymbol}"
            }
        }

    override operator fun contains(char: Char): Boolean =
        this.minimum <= char && char <= this.minimum

    override operator fun contains(charactersInterval: CharactersInterval): Boolean =
        when (charactersInterval)
        {
            is EmptyCharactersInterval  -> true
            is SimpleCharactersInterval ->
                (charactersInterval.minimum in this) && (charactersInterval.maximum in this)
            is UnionCharactersInterval  ->
                charactersInterval.simpleIntervals.all { interval -> interval in this }
        }
}

/**
 * Characters interval made of union of several [SimpleCharactersInterval]
 */
data class UnionCharactersInterval internal constructor(val simpleIntervals: List<SimpleCharactersInterval>) :
    CharactersInterval()
{
    override fun toString(): String =
        this.format()

    /**
     * Create a string representation of the interval.
     *
     * It is possible to customise header and footer different for case if only one character or several.
     *
     * Possible to customize the separator between minimum and maximum if more than one character.
     *
     * Possible to customize the union symbol
     *
     * Possible to print characters in `\uHHHH` version
     *
     * @param uniqueOpenSymbol Header to use if only one character
     * @param uniqueCloseSymbol Footer to use if only one character
     * @param openSymbol Header to use if more than one character
     * @param closeSymbol Footer to use if more than one character
     * @param separatorSymbol Separator between minimum and maximum if more than one character
     * @param unionSymbol symbol used to represents the union
     * @param unicode Indicates if show `\uHHHH` version or ordinary one
     */
    fun format(uniqueOpenSymbol: String = "{",
               uniqueCloseSymbol: String = "}",
               openSymbol: String = "[",
               closeSymbol: String = "]",
               separatorSymbol: String = ", ",
               unionSymbol: String = " U ",
               unicode: Boolean = false): String
    {
        if (this.simpleIntervals.isEmpty())
        {
            return "[]"
        }

        val stringBuilder = StringBuilder()
        stringBuilder.append(this.simpleIntervals[0].format(uniqueOpenSymbol,
                                                            uniqueCloseSymbol,
                                                            openSymbol,
                                                            closeSymbol,
                                                            separatorSymbol,
                                                            unicode))

        for (index in 1 until this.simpleIntervals.size)
        {
            stringBuilder.append(unionSymbol)
            stringBuilder.append(this.simpleIntervals[index].format(uniqueOpenSymbol,
                                                                    uniqueCloseSymbol,
                                                                    openSymbol,
                                                                    closeSymbol,
                                                                    separatorSymbol,
                                                                    unicode))
        }

        return stringBuilder.toString()
    }

    override operator fun contains(char: Char): Boolean =
        this.simpleIntervals.any { interval -> char in interval }

    override operator fun contains(charactersInterval: CharactersInterval): Boolean =
        when (charactersInterval)
        {
            is EmptyCharactersInterval  -> true
            is SimpleCharactersInterval ->
                (charactersInterval.minimum..charactersInterval.maximum).all { char -> char in this }
            is UnionCharactersInterval  ->
                charactersInterval.simpleIntervals.all { interval -> interval in this }
        }
}

/**
 * Create an interval between two characters.
 *
 * If given `minimum` if greater than `maximum`, [EmptyCharactersInterval] is returned
 *
 * If `minimum` equals to `maximum`, the interval will have only one character
 */
fun interval(minimum: Char, maximum: Char): CharactersInterval =
    if (minimum <= maximum)
    {
        SimpleCharactersInterval(minimum, maximum)
    }
    else
    {
        EmptyCharactersInterval
    }

/**
 * Interval made of lower case letters
 */
val lowerCaseInterval = interval('a', 'z')

/**
 * Interval made of upper case letters
 */
val upperCaseInterval = interval('A', 'Z')

/**
 * Interval made of lower or upper case letters
 */
val letterInterval = lowerCaseInterval + upperCaseInterval

/**
 * Interval of digits
 */
val digitInterval = interval('0', '9')

/**
 * Letter or digit interval
 */
val letterOrDigitInterval = letterInterval + digitInterval

/**
 * Letter, digit or underscore interval
 */
val letterOrDigitUnderscoreInterval = letterOrDigitInterval + '_'
