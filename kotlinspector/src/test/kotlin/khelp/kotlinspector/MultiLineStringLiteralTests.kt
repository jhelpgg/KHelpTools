package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.string.MultiLineStringLiteral
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MultiLineStringLiteralTests
{
    @Test
    fun emptyText()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\"\"\"",
                                                                        KotlinGrammar.multiLineStringLiteral))
        val multiLineStringLiteral = MultiLineStringLiteral()
        multiLineStringLiteral.parse(grammarNode)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertTrue( multilineStringContentOrExpressions.isEmpty())
    }

    @Test
    fun simpleText()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\nHello world !\n \t Hello folks! \t \n\"\"\"",
                                                                        KotlinGrammar.multiLineStringLiteral))
        val multiLineStringLiteral = MultiLineStringLiteral()
        multiLineStringLiteral.parse(grammarNode)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(1, multilineStringContentOrExpressions.size)
        val multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        val multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("\nHello world !\n \t Hello folks! \t \n", multiLineStrText.text)
    }

    @Test
    fun onlyQuotes()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\"\"\"\"",
                                                                        KotlinGrammar.multiLineStringLiteral))
        var multiLineStringLiteral = MultiLineStringLiteral()
        multiLineStringLiteral.parse(grammarNode)
        var multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(1, multilineStringContentOrExpressions.size)
        var multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\"\"\"\"\"",
                                                                    KotlinGrammar.multiLineStringLiteral))
        multiLineStringLiteral = MultiLineStringLiteral()
        multiLineStringLiteral.parse(grammarNode)
        multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(2, multilineStringContentOrExpressions.size)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[1].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\"\"\"\"\"\"",
                                                                    KotlinGrammar.multiLineStringLiteral))
        multiLineStringLiteral = MultiLineStringLiteral()
        multiLineStringLiteral.parse(grammarNode)
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
    fun stringReference()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\$value\"\"\"",
                                                                        KotlinGrammar.multiLineStringLiteral))
        val multiLineStringLiteral = MultiLineStringLiteral()
        multiLineStringLiteral.parse(grammarNode)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(1, multilineStringContentOrExpressions.size)
        val multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        val multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("value", multiLineStrRef.reference)
    }

    @Test
    fun expression()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\${computeResult()}\"\"\"",
                                                                        KotlinGrammar.multiLineStringLiteral))
        val multiLineStringLiteral = MultiLineStringLiteral()
        multiLineStringLiteral.parse(grammarNode)
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
    fun complex()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"The result is : \"\${computeResult()}\"\nParameters used : \$first,\$second, \$third\nDouble double quote \"\" should work\"\"\"",
                                                                        KotlinGrammar.multiLineStringLiteral))
        val multiLineStringLiteral = MultiLineStringLiteral()
        multiLineStringLiteral.parse(grammarNode)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(14, multilineStringContentOrExpressions.size)
        var multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        var multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("The result is : ",multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[1].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        val multiLineStringExpression = assumeNotNull(multilineStringContentOrExpressions[2].multiLineStringExpression)
        val postfixUnaryExpression = multiLineStringExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeResult",postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments().isEmpty())
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[3].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[4].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("\nParameters used : ",multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[5].multiLineStringContent)
        var multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("first", multiLineStrRef.reference)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[6].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals(",",multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[7].multiLineStringContent)
        multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("second", multiLineStrRef.reference)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[8].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals(", ",multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[9].multiLineStringContent)
        multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("third", multiLineStrRef.reference)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[10].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("\nDouble double quote ",multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[11].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[12].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[13].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals(" should work",multiLineStrText.text)
    }
}