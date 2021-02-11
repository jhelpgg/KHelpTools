package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.RangeExpression
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RangeExpressionTests
{
    @Test
    fun noRange()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("666",
                                                                        KotlinGrammar.rangeExpression))
        val rangeExpression = RangeExpression()
        rangeExpression.parse(grammarNode)
        val literalConstant = assumeNotNull(rangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("666", literalConstant.value)
    }

    @Test
    fun withRange()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("'g' .. 'u'",
                                                                        KotlinGrammar.rangeExpression))
        val rangeExpression = RangeExpression()
        rangeExpression.parse(grammarNode)
        var literalConstant = assumeNotNull(rangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.CHARACTER, literalConstant.literalConstantType)
        Assertions.assertEquals("'g'", literalConstant.value)
        literalConstant = assumeNotNull(rangeExpression.additiveExpressions()[1].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.CHARACTER, literalConstant.literalConstantType)
        Assertions.assertEquals("'u'", literalConstant.value)
    }
}