package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.string.LineStringLiteral
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LineStringLiteralTests
{
    @Test
    fun emptyText()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"",
                                                                        KotlinGrammar.lineStringLiteral))
        val lineStringLiteral = LineStringLiteral()
        lineStringLiteral.parse(grammarNode)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertTrue( lineStringContentOrExpressions.isEmpty())
    }

    @Test
    fun simpleText()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"Hello world ! \t Hello folks!\"",
                                                                        KotlinGrammar.lineStringLiteral))
        val lineStringLiteral = LineStringLiteral()
        lineStringLiteral.parse(grammarNode)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertEquals(1, lineStringContentOrExpressions.size)
        val lineStringContent = assumeNotNull(lineStringContentOrExpressions[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Hello world ! \t Hello folks!", lineStrText.text)
    }

    @Test
    fun stringReference()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\$value\"",
                                                                        KotlinGrammar.lineStringLiteral))
        val lineStringLiteral = LineStringLiteral()
        lineStringLiteral.parse(grammarNode)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertEquals(1, lineStringContentOrExpressions.size)
        val lineStringContent = assumeNotNull(lineStringContentOrExpressions[0].lineStringContent)
        val lineStrRef = assumeNotNull(lineStringContent.lineStrRef)
        Assertions.assertEquals("value", lineStrRef.reference)
    }

    @Test
    fun expression()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\${computeResult()}\"",
                                                                        KotlinGrammar.lineStringLiteral))
        val lineStringLiteral = LineStringLiteral()
        lineStringLiteral.parse(grammarNode)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertEquals(1, lineStringContentOrExpressions.size)
        val lineStringExpression = assumeNotNull(lineStringContentOrExpressions[0].lineStringExpression)
        val postfixUnaryExpression = lineStringExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeResult", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun complex()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"The result is : \${computeResult()}\tParameters used : \$first,\$second, \$third\"",
                                                                        KotlinGrammar.lineStringLiteral))
        val lineStringLiteral = LineStringLiteral()
        lineStringLiteral.parse(grammarNode)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertEquals(8, lineStringContentOrExpressions.size)
        var lineStringContent = assumeNotNull(lineStringContentOrExpressions[0].lineStringContent)
        var lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("The result is : ", lineStrText.text)
        val lineStringExpression = assumeNotNull(lineStringContentOrExpressions[1].lineStringExpression)
        val postfixUnaryExpression = lineStringExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeResult",postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments().isEmpty())
        lineStringContent = assumeNotNull(lineStringContentOrExpressions[2].lineStringContent)
        lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("\tParameters used : ", lineStrText.text)
        lineStringContent = assumeNotNull(lineStringContentOrExpressions[3].lineStringContent)
        var lineStrRef = assumeNotNull(lineStringContent.lineStrRef)
        Assertions.assertEquals("first", lineStrRef.reference)
        lineStringContent = assumeNotNull(lineStringContentOrExpressions[4].lineStringContent)
        lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals(",", lineStrText.text)
        lineStringContent = assumeNotNull(lineStringContentOrExpressions[5].lineStringContent)
        lineStrRef = assumeNotNull(lineStringContent.lineStrRef)
        Assertions.assertEquals("second", lineStrRef.reference)
        lineStringContent = assumeNotNull(lineStringContentOrExpressions[6].lineStringContent)
        lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals(", ", lineStrText.text)
        lineStringContent = assumeNotNull(lineStringContentOrExpressions[7].lineStringContent)
        lineStrRef = assumeNotNull(lineStringContent.lineStrRef)
        Assertions.assertEquals("third", lineStrRef.reference)
    }
}