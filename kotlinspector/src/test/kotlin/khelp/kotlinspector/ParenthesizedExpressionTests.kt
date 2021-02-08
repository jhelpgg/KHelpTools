package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.ParenthesizedExpression
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParenthesizedExpressionTests
{
    @Test
    fun expression()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("(operation(73, true))",
                                                                        KotlinGrammar.parenthesizedExpression))
        val parenthesizedExpression = ParenthesizedExpression()
        parenthesizedExpression.parse(grammarNode)
        val postfixUnaryExpression = parenthesizedExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("operation", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        var literalConstant = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
        literalConstant = assumeNotNull(callSuffix.valueArguments()[1].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BOOLEAN, literalConstant.literalConstantType)
        Assertions.assertEquals("true", literalConstant.value)
    }
}