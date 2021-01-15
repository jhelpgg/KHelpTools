package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.modifier.ParameterModifier
import khelp.kotlinspector.model.modifier.ParameterModifierType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParameterModifierTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("vararg", KotlinGrammar.parameterModifier)
        Assertions.assertNotNull(grammarNode)
        var parameterModifier = ParameterModifier()
        parameterModifier.parse(grammarNode!!)
        Assertions.assertEquals(ParameterModifierType.VARARG, parameterModifier.parameterModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("noinline", KotlinGrammar.parameterModifier)
        Assertions.assertNotNull(grammarNode)
        parameterModifier = ParameterModifier()
        parameterModifier.parse(grammarNode!!)
        Assertions.assertEquals(ParameterModifierType.NOINLINE, parameterModifier.parameterModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("crossinline", KotlinGrammar.parameterModifier)
        Assertions.assertNotNull(grammarNode)
        parameterModifier = ParameterModifier()
        parameterModifier.parse(grammarNode!!)
        Assertions.assertEquals(ParameterModifierType.CROSSINLINE, parameterModifier.parameterModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.parameterModifier)
        Assertions.assertNull(grammarNode)
    }
}