package khelp.utilities.regex

import khelp.utilities.extensions.plus
import khelp.utilities.extensions.regularExpression
import org.junit.Assert
import org.junit.Test

class RegularExpressionTests
{
    @Test
    fun justText()
    {
        val regularExpression = "Hello".regularExpression
        Assert.assertTrue(regularExpression.matches("Hello"))
        Assert.assertFalse(regularExpression.matches("HellO"))
        Assert.assertFalse(regularExpression.matches("World"))

        val replacement = regularExpression.replacement {
            +"Hi"
        }
        Assert.assertEquals("Hi every one. Hello world!", replacement.replaceFirst("Hello every one. Hello world!"))
        Assert.assertEquals("Hi every one. Hi world!", replacement.replaceAll("Hello every one. Hello world!"))

        val matcher = regularExpression.matcher("Hello every one. Hello world!")
        val stringBuilder = StringBuilder()
        Assert.assertTrue(matcher.find())
        Assert.assertEquals(0, matcher.start())
        Assert.assertEquals(5, matcher.end())
        matcher.appendReplacement(stringBuilder) {
            +"Hi"
        }
        Assert.assertTrue(matcher.find())
        Assert.assertEquals(17, matcher.start())
        Assert.assertEquals(22, matcher.end())
        matcher.appendReplacement(stringBuilder) {
            +"Hallo"
        }
        Assert.assertFalse(matcher.find())
        matcher.appendTail(stringBuilder)
        Assert.assertEquals("Hi every one. Hallo world!", stringBuilder.toString())
    }

    @Test
    fun groupReverse()
    {
        val first = WORD.group()
        val second = WORD.group()
        val regularExpression = first + WHITE_SPACE.oneOrMore() + "IS" + WHITE_SPACE.oneOrMore() + second
        val replacement = regularExpression.replacement {
            +second
            +" WAS "
            +first
        }
        Assert.assertEquals("red WAS color", replacement.replaceAll("color IS red"))
        Assert.assertEquals("[red WAS color] and [shoe WAS foot]",
                            replacement.replaceAll("[color IS red] and [foot IS shoe]"))
    }

    @Test
    fun reuseGroup()
    {
        val digitGroup = DIGIT.group()
        val passcodeTooSimple = digitGroup + digitGroup.zeroOrMore()
        Assert.assertTrue(passcodeTooSimple.matches("0"))
        Assert.assertTrue(passcodeTooSimple.matches("1111"))
        Assert.assertTrue(passcodeTooSimple.matches("55555"))
        Assert.assertTrue(passcodeTooSimple.matches("999999999"))
        Assert.assertFalse(passcodeTooSimple.matches("1191"))
    }

    @Test
    fun forEachMatchTest()
    {
        val first = WORD.group()
        val second = WORD.group()
        val regularExpression = '[' + first + WHITE_SPACE.oneOrMore() + "IS" + WHITE_SPACE.oneOrMore() + second + ']'
        val matcher = regularExpression.matcher("HEADER [First IS One] Intermediate1 [Second IS Two] Intermediate2 [Third IS Three] Tail")
        val result = matcher.forEachMatch(
            {
                println("Header=`${header}`")
                +"-H-"
            },
            {
                println("Intermediate=`${intermediate}`")
                +"-I-"
            },
            {
                println("Tail=`${tail}`")
                +"-T-"
            },
            {
                replace { matcher ->
                    {
                        println("first='${matcher.value(first)}'")
                        println("second='${matcher.value(second)}'")
                        +first
                        +"<=>"
                        +second
                    }
                }
            }
        )
        Assert.assertEquals("-H-First<=>One-I-Second<=>Two-I-Third<=>Three-T-", result)
    }
}