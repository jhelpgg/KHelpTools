package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.string.MultiLineStrText
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MultiLineStrTextTests
{
    @Test
    fun test()
    {
        var string = "Hello! Some \\\$ \nNew line ;)"
        var grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.MultiLineStrText)
        Assertions.assertNotNull(grammarNode)
        val multi = MultiLineStrText()
        multi.parse(grammarNode!!)
        Assertions.assertEquals(string, multi.text)

        string = "\"$\""
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.MultiLineStrText)
        Assertions.assertNull(grammarNode)
    }
}