package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.MultiplicativeExpression
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.MultiplicativeOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MultiplicativeExpressionTests
{
    @Test
    fun noMultiplication()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            if(found) 
            {
                println("I found it!")
            }
        """.trimIndent(),
                                                                        KotlinGrammar.multiplicativeExpression))
        val multiplicativeExpression = MultiplicativeExpression()
        multiplicativeExpression.parse(grammarNode)
        val ifExpression = assumeNotNull(multiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.ifExpression)
        Assertions.assertEquals("found",
                                ifExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val controlStructureBody = assumeNotNull(ifExpression.controlStructureBody)
        val block = assumeNotNull(controlStructureBody.block)
        val expression = assumeNotNull(block.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("println", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("I found it!", lineStrText.text)
    }

    @Test
    fun simpleMultiplication()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("42 * 73",
                                                                        KotlinGrammar.multiplicativeExpression))
        val multiplicativeExpression = MultiplicativeExpression()
        multiplicativeExpression.parse(grammarNode)
        var literalConstant = assumeNotNull(multiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        val (multiplicativeOperator, asExpression) = multiplicativeExpression.multiplicativeOperatorAsExpressionCouples()[0]
        Assertions.assertEquals(MultiplicativeOperator.TIMES, multiplicativeOperator)
        literalConstant = assumeNotNull(asExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
    }

    @Test
    fun complexMultiplication()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("42 * index / computeValue(73)",
                                                                        KotlinGrammar.multiplicativeExpression))
        val multiplicativeExpression = MultiplicativeExpression()
        multiplicativeExpression.parse(grammarNode)
        var literalConstant = assumeNotNull(multiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        val (multiplicativeOperator1, asExpression1) = multiplicativeExpression.multiplicativeOperatorAsExpressionCouples()[0]
        Assertions.assertEquals(MultiplicativeOperator.TIMES, multiplicativeOperator1)
        Assertions.assertEquals("index",
                                asExpression1.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (multiplicativeOperator2, asExpression2) = multiplicativeExpression.multiplicativeOperatorAsExpressionCouples()[1]
        Assertions.assertEquals(MultiplicativeOperator.DIVIDE, multiplicativeOperator2)
        val postfixUnaryExpression = asExpression2.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeValue", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        literalConstant = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
    }
}