package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.string.StringLiteral
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StringLiteralTests
{
    @Test
    fun emptyTextSimpleLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"",
                                                                        KotlinGrammar.stringLiteral))
        val stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertTrue(lineStringContentOrExpressions.isEmpty())
    }

    @Test
    fun emptyTextMultiLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\"\"\"",
                                                                        KotlinGrammar.stringLiteral))
        val stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        val multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertTrue(multilineStringContentOrExpressions.isEmpty())
    }

    @Test
    fun simpleTextSimpleLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"Hello world ! \t Hello folks!\"",
                                                                        KotlinGrammar.stringLiteral))
        val stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertEquals(1, lineStringContentOrExpressions.size)
        val lineStringContent = assumeNotNull(lineStringContentOrExpressions[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Hello world ! \t Hello folks!", lineStrText.text)
    }

    @Test
    fun simpleTextMultiLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\nHello world !\n \t Hello folks! \t \n\"\"\"",
                                                                        KotlinGrammar.stringLiteral))
        val stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        val multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(1, multilineStringContentOrExpressions.size)
        val multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        val multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("\nHello world !\n \t Hello folks! \t \n", multiLineStrText.text)
    }

    @Test
    fun stringReferenceSimpleLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\$value\"",
                                                                        KotlinGrammar.stringLiteral))
        val stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertEquals(1, lineStringContentOrExpressions.size)
        val lineStringContent = assumeNotNull(lineStringContentOrExpressions[0].lineStringContent)
        val lineStrRef = assumeNotNull(lineStringContent.lineStrRef)
        Assertions.assertEquals("value", lineStrRef.reference)
    }

    @Test
    fun onlyQuotesMultiLine()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\"\"\"\"",
                                                                        KotlinGrammar.stringLiteral))
        var stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        var multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        var multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(1, multilineStringContentOrExpressions.size)
        var multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\"\"\"\"\"",
                                                                    KotlinGrammar.stringLiteral))
        stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(2, multilineStringContentOrExpressions.size)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[1].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\"\"\"\"\"\"",
                                                                    KotlinGrammar.stringLiteral))
        stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(3, multilineStringContentOrExpressions.size)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[1].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[2].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
    }

    @Test
    fun stringReferenceMultiLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\$value\"\"\"",
                                                                        KotlinGrammar.stringLiteral))
        val stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        val multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(1, multilineStringContentOrExpressions.size)
        val multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        val multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("value", multiLineStrRef.reference)
    }

    @Test
    fun expressionSimpleLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\${computeResult()}\"",
                                                                        KotlinGrammar.stringLiteral))
        val stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
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
    fun expressionMultiLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\${computeResult()}\"\"\"",
                                                                        KotlinGrammar.stringLiteral))
        val stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        val multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(1, multilineStringContentOrExpressions.size)
        val multiLineStringExpression = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringExpression)
        val postfixUnaryExpression = multiLineStringExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeResult", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun complexSimpleLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"The result is : \${computeResult()}\tParameters used : \$first,\$second, \$third\"",
                                                                        KotlinGrammar.stringLiteral))
        val stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertEquals(8, lineStringContentOrExpressions.size)
        var lineStringContent = assumeNotNull(lineStringContentOrExpressions[0].lineStringContent)
        var lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("The result is : ", lineStrText.text)
        val lineStringExpression = assumeNotNull(lineStringContentOrExpressions[1].lineStringExpression)
        val postfixUnaryExpression = lineStringExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeResult", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
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

    @Test
    fun complexMultiLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"The result is : \"\${computeResult()}\"\nParameters used : \$first,\$second, \$third\nDouble double quote \"\" should work\"\"\"",
                                                                        KotlinGrammar.stringLiteral))
        val stringLiteral = StringLiteral()
        stringLiteral.parse(grammarNode)
        val multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(14, multilineStringContentOrExpressions.size)
        var multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        var multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("The result is : ", multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[1].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        val multiLineStringExpression = assumeNotNull(multilineStringContentOrExpressions[2].multiLineStringExpression)
        val postfixUnaryExpression = multiLineStringExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeResult", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[3].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[4].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("\nParameters used : ", multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[5].multiLineStringContent)
        var multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("first", multiLineStrRef.reference)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[6].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals(",", multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[7].multiLineStringContent)
        multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("second", multiLineStrRef.reference)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[8].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals(", ", multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[9].multiLineStringContent)
        multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("third", multiLineStrRef.reference)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[10].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("\nDouble double quote ", multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[11].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[12].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[13].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals(" should work", multiLineStrText.text)
    }
}