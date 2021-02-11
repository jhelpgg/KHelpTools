package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.InfixFunctionCall
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class InfixFunctionCallTests
{
    @Test
    fun noInfix()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("this.index",
                                                                        KotlinGrammar.infixFunctionCall))
        val infixFunctionCall = InfixFunctionCall()
        infixFunctionCall.parse(grammarNode)
        val postfixUnaryExpression = infixFunctionCall.firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        val thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
        val navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        val expression = assumeNotNull(navigationSuffix.expression)
        Assertions.assertEquals("index",
                                expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun withInfix()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("color and BLUE_MASK",
                                                                        KotlinGrammar.infixFunctionCall))
        val infixFunctionCall = InfixFunctionCall()
        infixFunctionCall.parse(grammarNode)
        Assertions.assertEquals("color",
                                infixFunctionCall.firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (name, rangeExpression) = infixFunctionCall.nameRangeExpressionCouples()[0]
        Assertions.assertEquals("and", name)
        Assertions.assertEquals("BLUE_MASK",
                                rangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }
}