package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.AdditiveExpression
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.AdditiveOperator
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AdditiveExpressionTests
{
    @Test
    fun noAddition()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("super.callMethod()",
                                                                        KotlinGrammar.additiveExpression))
        val additiveExpression = AdditiveExpression()
        additiveExpression.parse(grammarNode)
        var postfixUnaryExpression = additiveExpression.firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        val superExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.superExpression)
        Assertions.assertTrue(superExpression.simpleIdentifier.isEmpty())
        Assertions.assertNull(superExpression.type)
        val navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        val expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("callMethod", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun simpleAddition()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("42 + 73",
                                                                        KotlinGrammar.additiveExpression))
        val additiveExpression = AdditiveExpression()
        additiveExpression.parse(grammarNode)
        var literalConstant = assumeNotNull(additiveExpression.firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        val (additiveOperator, multiplicativeExpression) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[0]
        Assertions.assertEquals(AdditiveOperator.PLUS, additiveOperator)
        literalConstant = assumeNotNull(multiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
    }

    @Test
    fun complexAddition()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("666 - index + computeValue()",
                                                                        KotlinGrammar.additiveExpression))
        val additiveExpression = AdditiveExpression()
        additiveExpression.parse(grammarNode)
        val literalConstant = assumeNotNull(additiveExpression.firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("666", literalConstant.value)
        val (additiveOperator1, multiplicativeExpression1) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[0]
        Assertions.assertEquals(AdditiveOperator.MINUS, additiveOperator1)
        Assertions.assertEquals("index",
                                multiplicativeExpression1.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (additiveOperator2, multiplicativeExpression2) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[1]
        Assertions.assertEquals(AdditiveOperator.PLUS, additiveOperator2)
        val postfixUnaryExpression = multiplicativeExpression2.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeValue", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }
}