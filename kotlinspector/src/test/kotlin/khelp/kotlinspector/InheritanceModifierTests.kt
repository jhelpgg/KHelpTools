package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.modifier.InheritanceModifier
import khelp.kotlinspector.model.modifier.InheritanceModifierType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class InheritanceModifierTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("abstract", KotlinGrammar.inheritanceModifier)
        Assertions.assertNotNull(grammarNode)
        var inheritanceModifier = InheritanceModifier()
        inheritanceModifier.parse(grammarNode!!)
        Assertions.assertEquals(InheritanceModifierType.ABSTRACT, inheritanceModifier.inheritanceModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("final", KotlinGrammar.inheritanceModifier)
        Assertions.assertNotNull(grammarNode)
        inheritanceModifier = InheritanceModifier()
        inheritanceModifier.parse(grammarNode!!)
        Assertions.assertEquals(InheritanceModifierType.FINAL, inheritanceModifier.inheritanceModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("open", KotlinGrammar.inheritanceModifier)
        Assertions.assertNotNull(grammarNode)
        inheritanceModifier = InheritanceModifier()
        inheritanceModifier.parse(grammarNode!!)
        Assertions.assertEquals(InheritanceModifierType.OPEN, inheritanceModifier.inheritanceModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.inheritanceModifier)
        Assertions.assertNull(grammarNode)
    }
}