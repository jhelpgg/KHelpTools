package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.string.LineStringContent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LineStringContentTests
{
    @Test
    fun lineStringText()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("Hello world !",
                                                                        KotlinGrammar.lineStringContent))
        val lineStringContent = LineStringContent()
        lineStringContent.parse(grammarNode)
        Assertions.assertNull(lineStringContent.lineStrEscapedChar)
        Assertions.assertNull(lineStringContent.lineStrRef)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Hello world !", lineStrText.text)
    }

    @Test
    fun simpleEscapedCharacter()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\\n",
                                                                        KotlinGrammar.lineStringContent))
        val lineStringContent = LineStringContent()
        lineStringContent.parse(grammarNode)
        Assertions.assertNull(lineStringContent.lineStrText)
        Assertions.assertNull(lineStringContent.lineStrRef)
        val lineStrEscapedChar = assumeNotNull(lineStringContent.lineStrEscapedChar)
        Assertions.assertEquals("\\n", lineStrEscapedChar.escaped)
    }

    @Test
    fun unicodeEscapedCharacter()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\\uCAFE",
                                                                        KotlinGrammar.lineStringContent))
        val lineStringContent = LineStringContent()
        lineStringContent.parse(grammarNode)
        Assertions.assertNull(lineStringContent.lineStrText)
        Assertions.assertNull(lineStringContent.lineStrRef)
        val lineStrEscapedChar = assumeNotNull(lineStringContent.lineStrEscapedChar)
        Assertions.assertEquals("\\uCAFE", lineStrEscapedChar.escaped)
    }

    @Test
    fun stringReference()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\$value",
                                                                        KotlinGrammar.lineStringContent))
        val lineStringContent = LineStringContent()
        lineStringContent.parse(grammarNode)
        Assertions.assertNull(lineStringContent.lineStrText)
        Assertions.assertNull(lineStringContent.lineStrEscapedChar)
        val lineStrRef = assumeNotNull(lineStringContent.lineStrRef)
        Assertions.assertEquals("value", lineStrRef.reference)
    }
}