package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.InOperator
import khelp.kotlinspector.model.expression.test.RangeTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RangeTestTests
{
    @Test
    fun test()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("in list", KotlinGrammar.rangeTest))
        var rangeTest = RangeTest()
        rangeTest.parse(grammarNode)
        Assertions.assertEquals(InOperator.IN, rangeTest.inOperator)
        Assertions.assertEquals("list",
                                rangeTest.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("!in array", KotlinGrammar.rangeTest))
        rangeTest = RangeTest()
        rangeTest.parse(grammarNode)
        Assertions.assertEquals(InOperator.NOT_IN, rangeTest.inOperator)
        Assertions.assertEquals("array",
                                rangeTest.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }
}