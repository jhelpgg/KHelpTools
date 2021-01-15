package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.string.LineStrRef
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LineStrRefTests
{
    @Test
    fun test()
    {
        var string = "\$index"
        var grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.LineStrRef)
        Assertions.assertNotNull(grammarNode)
        val multi = LineStrRef()
        multi.parse(grammarNode!!)
        Assertions.assertEquals("index", multi.reference)

        string = "value"
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.LineStrRef)
        Assertions.assertNull(grammarNode)
    }
}