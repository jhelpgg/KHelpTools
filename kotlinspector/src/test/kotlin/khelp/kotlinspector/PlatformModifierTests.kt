package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.modifier.PlatformModifier
import khelp.kotlinspector.model.modifier.PlatformModifierType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PlatformModifierTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("expect", KotlinGrammar.platformModifier)
        Assertions.assertNotNull(grammarNode)
        var platformModifier = PlatformModifier()
        platformModifier.parse(grammarNode!!)
        Assertions.assertEquals(PlatformModifierType.EXPECT, platformModifier.platformModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("actual", KotlinGrammar.platformModifier)
        Assertions.assertNotNull(grammarNode)
        platformModifier = PlatformModifier()
        platformModifier.parse(grammarNode!!)
        Assertions.assertEquals(PlatformModifierType.ACTUAL, platformModifier.platformModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.platformModifier)
        Assertions.assertNull(grammarNode)
    }
}