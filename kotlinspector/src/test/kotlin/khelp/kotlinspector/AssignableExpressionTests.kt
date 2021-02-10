package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.AssignableExpression
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AssignableExpressionTests
{
    @Test
    fun prefixUnaryExpression()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("true",
                                                                        KotlinGrammar.assignableExpression))
        val assignableExpression = AssignableExpression()
        assignableExpression.parse(grammarNode)
        val prefixUnaryExpression = assumeNotNull(assignableExpression.prefixUnaryExpression)
        val literalConstant = assumeNotNull(prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BOOLEAN, literalConstant.literalConstantType)
        Assertions.assertEquals("true", literalConstant.value)
    }

    @Test
    fun simpleParenthesisPrefixUnaryExpression()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("(42)",
                                                                        KotlinGrammar.assignableExpression))
        val assignableExpression = AssignableExpression()
        assignableExpression.parse(grammarNode)
        val parenthesizedAssignableExpression = assumeNotNull(assignableExpression.parenthesizedAssignableExpression)
        val prefixUnaryExpression = assumeNotNull(parenthesizedAssignableExpression.assignableExpression.prefixUnaryExpression)
        val literalConstant = assumeNotNull(prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
    }

    @Test
    fun embedParenthesis()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("((indexOf(\"Test\")))",
                                                                        KotlinGrammar.assignableExpression))
        val assignableExpression = AssignableExpression()
        assignableExpression.parse(grammarNode)
        var parenthesizedAssignableExpression = assumeNotNull(assignableExpression.parenthesizedAssignableExpression)
        parenthesizedAssignableExpression = assumeNotNull(parenthesizedAssignableExpression.assignableExpression.parenthesizedAssignableExpression)
        val prefixUnaryExpression = assumeNotNull(parenthesizedAssignableExpression.assignableExpression.prefixUnaryExpression)
        val postfixUnaryExpression = prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("indexOf", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Test", lineStrText.text)
    }
}