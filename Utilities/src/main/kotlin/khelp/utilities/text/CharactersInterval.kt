package khelp.utilities.text

import khelp.utilities.extensions.plus
import khelp.utilities.extensions.toUnicode

sealed class CharactersInterval
{
    abstract operator fun contains(char: Char): Boolean

    abstract operator fun contains(charactersInterval: CharactersInterval): Boolean
}

object EmptyCharactersInterval : CharactersInterval()
{
    override fun toString(): String = "[]"

    override operator fun contains(char: Char): Boolean = false

    override operator fun contains(charactersInterval: CharactersInterval): Boolean =
        charactersInterval == EmptyCharactersInterval
}

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

data class UnionCharactersInterval internal constructor(val simpleIntervals: List<SimpleCharactersInterval>) :
    CharactersInterval()
{
    override fun toString(): String =
        this.format()

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

fun interval(character1: Char, character2: Char): CharactersInterval =
    if (character1 <= character2)
    {
        SimpleCharactersInterval(character1, character2)
    }
    else
    {
        EmptyCharactersInterval
    }

val lowerCaseInterval = interval('a', 'z')

val upperCaseInterval = interval('A', 'Z')

val letterInterval = lowerCaseInterval + upperCaseInterval

val digitInterval = interval('0', '9')

val letterOrDigitInterval = letterInterval + digitInterval

val letterOrDigitUnderscoreInterval = letterOrDigitInterval + '_'

