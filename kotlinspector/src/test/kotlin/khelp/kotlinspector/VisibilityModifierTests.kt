package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.modifier.VisibilityModifier
import khelp.kotlinspector.model.modifier.VisibilityModifierType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class VisibilityModifierTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("public", KotlinGrammar.visibilityModifier)
        Assertions.assertNotNull(grammarNode)
        var visibilityModifier = VisibilityModifier()
        visibilityModifier.parse(grammarNode!!)
        Assertions.assertEquals(VisibilityModifierType.PUBLIC, visibilityModifier.visibilityModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("private", KotlinGrammar.visibilityModifier)
        Assertions.assertNotNull(grammarNode)
        visibilityModifier = VisibilityModifier()
        visibilityModifier.parse(grammarNode!!)
        Assertions.assertEquals(VisibilityModifierType.PRIVATE, visibilityModifier.visibilityModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("internal", KotlinGrammar.visibilityModifier)
        Assertions.assertNotNull(grammarNode)
        visibilityModifier = VisibilityModifier()
        visibilityModifier.parse(grammarNode!!)
        Assertions.assertEquals(VisibilityModifierType.INTERNAL, visibilityModifier.visibilityModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("protected", KotlinGrammar.visibilityModifier)
        Assertions.assertNotNull(grammarNode)
        visibilityModifier = VisibilityModifier()
        visibilityModifier.parse(grammarNode!!)
        Assertions.assertEquals(VisibilityModifierType.PROTECTED, visibilityModifier.visibilityModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.visibilityModifier)
        Assertions.assertNull(grammarNode)
    }
}