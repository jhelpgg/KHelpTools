package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.modifier.ParameterModifierType
import khelp.kotlinspector.model.modifier.ParameterModifiers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParameterModifiersTests
{
    @Test
    fun annotationTest()
    {
        val grammarNode = KotlinGrammar.parseSpecificRule("@Test", KotlinGrammar.parameterModifiers)
        Assertions.assertNotNull(grammarNode)
        val parameterModifiers = ParameterModifiers()
        parameterModifiers.parse(grammarNode!!)
        Assertions.assertEquals("@Test", parameterModifiers.annotation)
        Assertions.assertEquals(0, parameterModifiers.parameterModifiers().size)
    }

    @Test
    fun parameterModifierTest()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("vararg", KotlinGrammar.parameterModifiers)
        Assertions.assertNotNull(grammarNode)
        var parameterModifiers = ParameterModifiers()
        parameterModifiers.parse(grammarNode!!)
        Assertions.assertEquals("", parameterModifiers.annotation)
        var parameterModifiersArray = parameterModifiers.parameterModifiers()
        Assertions.assertEquals(1, parameterModifiersArray.size)
        Assertions.assertEquals(ParameterModifierType.VARARG, parameterModifiersArray[0].parameterModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("noinline vararg", KotlinGrammar.parameterModifiers)
        Assertions.assertNotNull(grammarNode)
        parameterModifiers = ParameterModifiers()
        parameterModifiers.parse(grammarNode!!)
        Assertions.assertEquals("", parameterModifiers.annotation)
        parameterModifiersArray = parameterModifiers.parameterModifiers()
        Assertions.assertEquals(2, parameterModifiersArray.size)
        Assertions.assertEquals(ParameterModifierType.NOINLINE, parameterModifiersArray[0].parameterModifierType)
        Assertions.assertEquals(ParameterModifierType.VARARG, parameterModifiersArray[1].parameterModifierType)
    }
}