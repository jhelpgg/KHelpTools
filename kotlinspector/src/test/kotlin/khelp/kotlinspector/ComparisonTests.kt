package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.Comparison
import khelp.kotlinspector.model.expression.operator.ComparisonOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ComparisonTests
{
    @Test
    fun lowerTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("value < maximum",
                                                                        KotlinGrammar.comparison))
        val comparison = Comparison()
        comparison.parse(grammarNode)
        Assertions.assertEquals("value",
                                comparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (operator, genericCallLikeComparison) = comparison.operatorGenericCallLikeComparisonCouples()[0]
        Assertions.assertEquals(ComparisonOperator.LOWER, operator)
        Assertions.assertEquals("maximum",
                                genericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }
}