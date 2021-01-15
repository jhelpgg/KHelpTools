package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HiddenTests
{
    @Test
    fun commentMultiline()
    {
        var string = "/**/"
        var grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.Hidden)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(string, grammarNode!!.text)

        string = "/* Something */"
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.Hidden)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(string, grammarNode!!.text)

        string = "/*\nFirst line\nSecond line\n*/"
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.Hidden)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(string, grammarNode!!.text)

        string = "/**\n* Documentation is not only coool\n* It should be mandatory\n@param parameter A parameter\n*/"
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.Hidden)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(string, grammarNode!!.text)

        string = "/* not closed"
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.Hidden)
        Assertions.assertNull(grammarNode)

        string = "not open */"
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.Hidden)
        Assertions.assertNull(grammarNode)
    }

    @Test
    fun commentOneLine()
    {
        var string = "// One line comment\n"
        var grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.Hidden)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(string, grammarNode!!.text)

        string = "// One line comment \n One line we said !"
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.Hidden)
        Assertions.assertNull(grammarNode)

        string = "One line"
        grammarNode = KotlinGrammar.parseSpecificRule(string, KotlinGrammar.Hidden)
        Assertions.assertNull(grammarNode)
    }
}