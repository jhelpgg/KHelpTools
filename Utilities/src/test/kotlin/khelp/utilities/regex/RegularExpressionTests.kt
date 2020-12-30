package khelp.utilities.regex

import khelp.utilities.extensions.anyNonWhiteSpaceExceptThis
import khelp.utilities.extensions.anyWordExceptThis
import khelp.utilities.extensions.plus
import khelp.utilities.extensions.regularExpression
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RegularExpressionTests
{
    @Test
    fun justText()
    {
        val regularExpression = "Hello".regularExpression
        Assertions.assertTrue(regularExpression.matches("Hello"))
        Assertions.assertFalse(regularExpression.matches("HellO"))
        Assertions.assertFalse(regularExpression.matches("World"))

        val replacement = regularExpression.replacement {
            +"Hi"
        }
        Assertions.assertEquals("Hi every one. Hello world!", replacement.replaceFirst("Hello every one. Hello world!"))
        Assertions.assertEquals("Hi every one. Hi world!", replacement.replaceAll("Hello every one. Hello world!"))

        val matcher = regularExpression.matcher("Hello every one. Hello world!")
        val stringBuilder = StringBuilder()
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals(0, matcher.start())
        Assertions.assertEquals(5, matcher.end())
        matcher.appendReplacement(stringBuilder) {
            +"Hi"
        }
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals(17, matcher.start())
        Assertions.assertEquals(22, matcher.end())
        matcher.appendReplacement(stringBuilder) {
            +"Hallo"
        }
        Assertions.assertFalse(matcher.find())
        matcher.appendTail(stringBuilder)
        Assertions.assertEquals("Hi every one. Hallo world!", stringBuilder.toString())
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
        Assertions.assertEquals("red WAS color", replacement.replaceAll("color IS red"))
        Assertions.assertEquals("[red WAS color] and [shoe WAS foot]",
                                replacement.replaceAll("[color IS red] and [foot IS shoe]"))
    }

    @Test
    fun reuseGroup()
    {
        val digitGroup = DIGIT.group()
        val passcodeTooSimple = digitGroup + digitGroup.zeroOrMore()
        Assertions.assertTrue(passcodeTooSimple.matches("0"))
        Assertions.assertTrue(passcodeTooSimple.matches("1111"))
        Assertions.assertTrue(passcodeTooSimple.matches("55555"))
        Assertions.assertTrue(passcodeTooSimple.matches("999999999"))
        Assertions.assertFalse(passcodeTooSimple.matches("1191"))
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
        Assertions.assertEquals("-H-First<=>One-I-Second<=>Two-I-Third<=>Three-T-", result)
    }

    @Test
    fun exceptWordTest()
    {
        val text = "I remove the store. So I am a store remover. Only 'remove' should be removed"
        val matcher = "remove".anyWordExceptThis.matcher(text)
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("I", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("the", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("store", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("So", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("I", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("am", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("a", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("store", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("remover", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("Only", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("should", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("be", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("removed", text.substring(matcher.start(), matcher.end()))
        Assertions.assertFalse(matcher.find())
    }

    @Test
    fun exceptNonWhiteSpaceTest()
    {
        val text = "I remove the store. So I am a store remover. Only 'remove' should be removed"
        val matcher = "remove".anyNonWhiteSpaceExceptThis.matcher(text)
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("I", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("the", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("store.", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("So", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("I", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("am", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("a", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("store", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("remover.", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("Only", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("'remove'", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("should", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("be", text.substring(matcher.start(), matcher.end()))
        Assertions.assertTrue(matcher.find())
        Assertions.assertEquals("removed", text.substring(matcher.start(), matcher.end()))
        Assertions.assertFalse(matcher.find())
    }
}