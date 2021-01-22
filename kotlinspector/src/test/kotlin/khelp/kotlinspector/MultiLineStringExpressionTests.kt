package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.string.MultiLineStringExpression
import khelp.kotlinspector.model.expression.operator.AdditiveOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MultiLineStringExpressionTests
{
    @Test
    fun simpleReference()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\${value}",
                                                                        KotlinGrammar.multiLineStringExpression))
        val multiLineStringExpression = MultiLineStringExpression()
        multiLineStringExpression.parse(grammarNode)
        Assertions.assertEquals("value",
                                multiLineStringExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun operation()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\${ first + second }",
                                                                        KotlinGrammar.multiLineStringExpression))
        val multiLineStringExpression = MultiLineStringExpression()
        multiLineStringExpression.parse(grammarNode)
        val additiveExpression = multiLineStringExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0]
        Assertions.assertEquals("first",
                                additiveExpression.firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (additiveOperator, multiplicativeExpression) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[0]
        Assertions.assertEquals(AdditiveOperator.PLUS, additiveOperator)
        Assertions.assertEquals("second",
                                multiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }
}