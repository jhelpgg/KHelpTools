package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.string.LineStrEscapedChar
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LineStrEscapedCharTests
{
    @Test
    fun test()
    {
        var string = "\\n"
        var grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.LineStrEscapedChar)
        Assertions.assertNotNull(grammarNode)
        var line = LineStrEscapedChar()
        line.parse(grammarNode!!)
        Assertions.assertEquals(string, line.escaped)

        string = "\\ucafe"
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.LineStrEscapedChar)
        Assertions.assertNotNull(grammarNode)
        line = LineStrEscapedChar()
        line.parse(grammarNode!!)
        Assertions.assertEquals(string, line.escaped)

        string = "something"
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.LineStrEscapedChar)
        Assertions.assertNull(grammarNode)
    }
}