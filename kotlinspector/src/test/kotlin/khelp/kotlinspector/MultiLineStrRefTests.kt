package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.string.MultiLineStrRef
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MultiLineStrRefTests
{
    @Test
    fun test()
    {
        var string = "\$index"
        var grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.MultiLineStrRef)
        Assertions.assertNotNull(grammarNode)
        val multi = MultiLineStrRef()
        multi.parse(grammarNode!!)
        Assertions.assertEquals("index", multi.reference)

        string = "value"
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.MultiLineStrRef)
        Assertions.assertNull(grammarNode)
    }
}