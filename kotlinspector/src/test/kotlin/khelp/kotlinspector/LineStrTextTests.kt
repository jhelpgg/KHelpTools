package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.string.LineStrText
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LineStrTextTests
{
    @Test
    fun test()
    {
        var string = "Hello! Some \\\$ \nNew line ;)"
        var grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.LineStrText)
        Assertions.assertNotNull(grammarNode)
        val multi = LineStrText()
        multi.parse(grammarNode!!)
        Assertions.assertEquals(string, multi.text)

        string = "\"$\""
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.LineStrText)
        Assertions.assertNull(grammarNode)
    }
}