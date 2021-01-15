package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.type.TypeModifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TypeModifierTests
{
    @Test
    fun annotationTest()
    {
        val grammarNode = KotlinGrammar.parseSpecificRule("@Test", KotlinGrammar.typeModifier)
        Assertions.assertNotNull(grammarNode)
        val typeModifier = TypeModifier()
        typeModifier.parse(grammarNode!!)
        Assertions.assertEquals("@Test", typeModifier.annotation)
        Assertions.assertFalse(typeModifier.isSuspend)
    }

    @Test
    fun suspendTest()
    {
        val grammarNode = KotlinGrammar.parseSpecificRule("suspend", KotlinGrammar.typeModifier)
        Assertions.assertNotNull(grammarNode)
        val typeModifier = TypeModifier()
        typeModifier.parse(grammarNode!!)
        Assertions.assertEquals("", typeModifier.annotation)
        Assertions.assertTrue(typeModifier.isSuspend)
    }
}