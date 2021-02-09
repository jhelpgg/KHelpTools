package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.ParenthesizedAssignableExpression
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParenthesizedAssignableExpressionTests
{
    @Test
    fun simplePrefixUnaryExpression()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("(42)",
                                                                        KotlinGrammar.parenthesizedAssignableExpression))
        val parenthesizedAssignableExpression = ParenthesizedAssignableExpression()
        parenthesizedAssignableExpression.parse(grammarNode)
        val prefixUnaryExpression = assumeNotNull(parenthesizedAssignableExpression.assignableExpression.prefixUnaryExpression)
        val literalConstant = assumeNotNull(prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
    }

    @Test
    fun embedParenthesis()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("((indexOf(\"Test\")))",
                                                                        KotlinGrammar.parenthesizedAssignableExpression))
        val parenthesizedAssignableExpression = ParenthesizedAssignableExpression()
        parenthesizedAssignableExpression.parse(grammarNode)
        val prefixUnaryExpression = assumeNotNull(parenthesizedAssignableExpression.assignableExpression.prefixUnaryExpression)
        var postfixUnaryExpression = prefixUnaryExpression.postfixUnaryExpression
        val parenthesizedExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.parenthesizedExpression)
        postfixUnaryExpression = parenthesizedExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("indexOf", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Test", lineStrText.text)
    }
}