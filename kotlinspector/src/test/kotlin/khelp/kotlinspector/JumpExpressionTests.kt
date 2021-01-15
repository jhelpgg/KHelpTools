package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.JumpExpression
import khelp.kotlinspector.model.expression.JumpExpressionType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class JumpExpressionTests
{
    @Test
    fun throwTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("throw IllegalArgumentException(\"message\")",
                                                                        KotlinGrammar.jumpExpression))
        val jumpExpression = JumpExpression()
        jumpExpression.parse(grammarNode)
        Assertions.assertEquals(JumpExpressionType.THROW, jumpExpression.jumpExpressionType)
        Assertions.assertTrue(jumpExpression.atName.isEmpty())
        val expression = assumeNotNull(jumpExpression.expression)
        val firstGenericCallLikeComparison = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison
        val postfixUnaryExpression = firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        val name = postfixUnaryExpression.primaryExpression.identifier
        Assertions.assertEquals("IllegalArgumentException", name)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("message", lineStrText.text)
    }

    @Test
    fun returnTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("return String(\"message\")",
                                                                        KotlinGrammar.jumpExpression))
        val jumpExpression = JumpExpression()
        jumpExpression.parse(grammarNode)
        Assertions.assertEquals(JumpExpressionType.RETURN, jumpExpression.jumpExpressionType)
        Assertions.assertTrue(jumpExpression.atName.isEmpty())
        val expression = assumeNotNull(jumpExpression.expression)
        val firstGenericCallLikeComparison = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison
        val postfixUnaryExpression = firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        val name = postfixUnaryExpression.primaryExpression.identifier
        Assertions.assertEquals("String", name)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("message", lineStrText.text)
    }

    @Test
    fun returnAtTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("return@returnAtTest String(\"message\")",
                                                                        KotlinGrammar.jumpExpression))
        val jumpExpression = JumpExpression()
        jumpExpression.parse(grammarNode)
        Assertions.assertEquals(JumpExpressionType.RETURN, jumpExpression.jumpExpressionType)
        Assertions.assertEquals("returnAtTest", jumpExpression.atName)
        val expression = assumeNotNull(jumpExpression.expression)
        val firstGenericCallLikeComparison = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison
        val postfixUnaryExpression = firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        val name = postfixUnaryExpression.primaryExpression.identifier
        Assertions.assertEquals("String", name)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("message", lineStrText.text)
    }

    @Test
    fun continueTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("continue",
                                                                        KotlinGrammar.jumpExpression))
        val jumpExpression = JumpExpression()
        jumpExpression.parse(grammarNode)
        Assertions.assertEquals(JumpExpressionType.CONTINUE, jumpExpression.jumpExpressionType)
        Assertions.assertTrue(jumpExpression.atName.isEmpty())
        Assertions.assertNull(jumpExpression.expression)
    }

    @Test
    fun continueAtTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("continue@somewhere",
                                                                        KotlinGrammar.jumpExpression))
        val jumpExpression = JumpExpression()
        jumpExpression.parse(grammarNode)
        Assertions.assertEquals(JumpExpressionType.CONTINUE, jumpExpression.jumpExpressionType)
        Assertions.assertEquals("somewhere", jumpExpression.atName)
        Assertions.assertNull(jumpExpression.expression)
    }

    @Test
    fun breakTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("break",
                                                                        KotlinGrammar.jumpExpression))
        val jumpExpression = JumpExpression()
        jumpExpression.parse(grammarNode)
        Assertions.assertEquals(JumpExpressionType.BREAK, jumpExpression.jumpExpressionType)
        Assertions.assertTrue(jumpExpression.atName.isEmpty())
        Assertions.assertNull(jumpExpression.expression)
    }

    @Test
    fun breakAtTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("break@somewhere",
                                                                        KotlinGrammar.jumpExpression))
        val jumpExpression = JumpExpression()
        jumpExpression.parse(grammarNode)
        Assertions.assertEquals(JumpExpressionType.BREAK, jumpExpression.jumpExpressionType)
        Assertions.assertEquals("somewhere", jumpExpression.atName)
        Assertions.assertNull(jumpExpression.expression)
    }
}