package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.InfixOperation
import khelp.kotlinspector.model.expression.operator.InOperator
import khelp.kotlinspector.model.expression.operator.IsOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class InfixOperationTests
{
    @Test
    fun inOperator()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("element in list",
                                                                        KotlinGrammar.infixOperation))
        val infixOperation = InfixOperation()
        infixOperation.parse(grammarNode)
        val infixFunctionCall = infixOperation.elvisExpression.infixFunctionCalls()[0]
        Assertions.assertEquals("element",
                                infixFunctionCall.firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (name, rangeExpression) = infixFunctionCall.nameRangeExpressionCouples()[0]
        Assertions.assertEquals(InOperator.IN, InOperator.parse(name))
        Assertions.assertEquals("list",
                                rangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun isOperator()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("animal is Bird",
                                                                        KotlinGrammar.infixOperation))
        val infixOperation = InfixOperation()
        infixOperation.parse(grammarNode)
        val infixFunctionCall = infixOperation.elvisExpression.infixFunctionCalls()[0]
        Assertions.assertEquals("animal",
                                infixFunctionCall.firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (name, rangeExpression) = infixFunctionCall.nameRangeExpressionCouples()[0]
        Assertions.assertEquals(IsOperator.IS, IsOperator.parse(name))
        Assertions.assertEquals("Bird",
                                rangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }
}