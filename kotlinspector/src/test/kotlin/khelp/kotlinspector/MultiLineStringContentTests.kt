package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.string.MultiLineStringContent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MultiLineStringContentTests
{
    @Test
    fun simpleText()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("Hello world, \\$ for test",
                                                                        KotlinGrammar.multiLineStringContent))
        val multiLineStringContent = MultiLineStringContent()
        multiLineStringContent.parse(grammarNode)
        Assertions.assertNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertFalse(multiLineStringContent.isQuote)
        val multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("Hello world, \\$ for test", multiLineStrText.text)
    }

    @Test
    fun justQuote()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"",
                                                                        KotlinGrammar.multiLineStringContent))
        val multiLineStringContent = MultiLineStringContent()
        multiLineStringContent.parse(grammarNode)
        Assertions.assertNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        Assertions.assertNull(multiLineStringContent.multiLineStrText)
    }

    @Test
    fun reference()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\$value",
                                                                        KotlinGrammar.multiLineStringContent))

        val multiLineStringContent = MultiLineStringContent()
        multiLineStringContent.parse(grammarNode)
        Assertions.assertFalse(multiLineStringContent.isQuote)
        Assertions.assertNull(multiLineStringContent.multiLineStrText)
        val multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("value", multiLineStrRef.reference)
    }
}