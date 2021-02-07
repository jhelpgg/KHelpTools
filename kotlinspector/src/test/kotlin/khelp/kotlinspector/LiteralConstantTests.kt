package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.LiteralConstant
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LiteralConstantTests
{
    @Test
    fun booleanLiteral()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("true",
                                                                        KotlinGrammar.literalConstant))
        var literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.BOOLEAN, literalConstant.literalConstantType)
        Assertions.assertEquals("true", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("false",
                                                                    KotlinGrammar.literalConstant))
        literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.BOOLEAN, literalConstant.literalConstantType)
        Assertions.assertEquals("false", literalConstant.value)
    }

    @Test
    fun unsignedLiteral()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("42UL",
                                                                        KotlinGrammar.literalConstant))
        var literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.UNSIGNED, literalConstant.literalConstantType)
        Assertions.assertEquals("42UL", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0xCAFEu",
                                                                    KotlinGrammar.literalConstant))
        literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.UNSIGNED, literalConstant.literalConstantType)
        Assertions.assertEquals("0xCAFEu", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0b1001ul",
                                                                    KotlinGrammar.literalConstant))
        literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.UNSIGNED, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1001ul", literalConstant.value)

        Assertions.assertNull(KotlinGrammar.parseSpecificRule("73uv",
                                                              KotlinGrammar.literalConstant))

        Assertions.assertNull(KotlinGrammar.parseSpecificRule("666ule",
                                                              KotlinGrammar.literalConstant))
    }

    @Test
    fun longLiteral()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("42L",
                                                                        KotlinGrammar.literalConstant))
        var literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.LONG, literalConstant.literalConstantType)
        Assertions.assertEquals("42L", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0xCAFEl",
                                                                    KotlinGrammar.literalConstant))
        literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.LONG, literalConstant.literalConstantType)
        Assertions.assertEquals("0xCAFEl", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0b1001L",
                                                                    KotlinGrammar.literalConstant))
        literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.LONG, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1001L", literalConstant.value)

        Assertions.assertNull(KotlinGrammar.parseSpecificRule("73LA",
                                                              KotlinGrammar.literalConstant))
    }

    @Test
    fun hexLiteral()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0xFacE09",
                                                                        KotlinGrammar.literalConstant))
        val literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.HEXADECIMAL, literalConstant.literalConstantType)
        Assertions.assertEquals("0xFacE09", literalConstant.value)
    }

    @Test
    fun binLiteral()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0b1001",
                                                                        KotlinGrammar.literalConstant))
        val literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1001", literalConstant.value)
    }

    @Test
    fun characterLiteral()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("'O'",
                                                                        KotlinGrammar.literalConstant))
        var literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.CHARACTER, literalConstant.literalConstantType)
        Assertions.assertEquals("'O'", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("'\\n'",
                                                                    KotlinGrammar.literalConstant))
        literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.CHARACTER, literalConstant.literalConstantType)
        Assertions.assertEquals("'\\n'", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("'\\u1AB2'",
                                                                    KotlinGrammar.literalConstant))
        literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.CHARACTER, literalConstant.literalConstantType)
        Assertions.assertEquals("'\\u1AB2'", literalConstant.value)
    }

    @Test
    fun realLiteral()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0.123456789",
                                                                        KotlinGrammar.literalConstant))
        var literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.REAL, literalConstant.literalConstantType)
        Assertions.assertEquals("0.123456789", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("1e-9",
                                                                    KotlinGrammar.literalConstant))
        literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.REAL, literalConstant.literalConstantType)
        Assertions.assertEquals("1e-9", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("-73.42",
                                                                    KotlinGrammar.literalConstant))
        literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.REAL, literalConstant.literalConstantType)
        Assertions.assertEquals("-73.42", literalConstant.value)
    }

    @Test
    fun nullLiteral()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("null",
                                                                        KotlinGrammar.literalConstant))
        val literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.NULL, literalConstant.literalConstantType)
        Assertions.assertEquals("null", literalConstant.value)
    }

    @Test
    fun integerLiteral()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("73",
                                                                        KotlinGrammar.literalConstant))
        var literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("-42",
                                                                    KotlinGrammar.literalConstant))
        literalConstant = LiteralConstant()
        literalConstant.parse(grammarNode)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("-42", literalConstant.value)
    }
}