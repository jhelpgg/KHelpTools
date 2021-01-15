package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.modifier.MemberModifier
import khelp.kotlinspector.model.modifier.MemberModifierType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MemberModifierTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("override", KotlinGrammar.memberModifier)
        Assertions.assertNotNull(grammarNode)
        var memberModifier = MemberModifier()
        memberModifier.parse(grammarNode!!)
        Assertions.assertEquals(MemberModifierType.OVERRIDE, memberModifier.memberModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("lateinit", KotlinGrammar.memberModifier)
        Assertions.assertNotNull(grammarNode)
        memberModifier = MemberModifier()
        memberModifier.parse(grammarNode!!)
        Assertions.assertEquals(MemberModifierType.LATEINIT, memberModifier.memberModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.memberModifier)
        Assertions.assertNull(grammarNode)
    }
}