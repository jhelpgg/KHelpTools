package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.ValueArgument
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ToolsTests
{
    @Test
    fun valueArgumentsEmpty()
    {
        val valueArguments = ArrayList<ValueArgument>()
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("()",
                                                                        KotlinGrammar.valueArguments))
        fillValueArguments(grammarNode, valueArguments)
        Assertions.assertTrue(valueArguments.isEmpty())
    }

    @Test
    fun valueArgumentsTest()
    {
        val valueArguments = ArrayList<ValueArgument>()
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("(\"test\", index = 42)",
                                                                        KotlinGrammar.valueArguments))
        fillValueArguments(grammarNode, valueArguments)
        Assertions.assertEquals(2, valueArguments.size)
        val stringLiteral = assumeNotNull(valueArguments[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("test", lineStrText.text)
        val valueArgument = valueArguments[1]
        Assertions.assertEquals("index", valueArgument.name)
        val literalConstant = assumeNotNull(valueArgument.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
    }
}