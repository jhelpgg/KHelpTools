package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.modifier.ClassModifier
import khelp.kotlinspector.model.modifier.ClassModifierType
import khelp.kotlinspector.model.modifier.Modifier
import khelp.kotlinspector.model.modifier.VisibilityModifier
import khelp.kotlinspector.model.modifier.VisibilityModifierType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ModifiersTests
{
    @Test
    fun annotationTest()
    {
        val grammarNode = KotlinGrammar.parseSpecificRule("@Test", KotlinGrammar.modifiers)
        Assertions.assertNotNull(grammarNode)
        val modifiers = ArrayList<Modifier>()
        val annotation = fillModifiersAndReturnAnnotation(grammarNode!!, modifiers)
        Assertions.assertEquals("@Test", annotation)
        Assertions.assertTrue(modifiers.isEmpty())
    }

    @Test
    fun simpleTest()
    {
        val grammarNode = KotlinGrammar.parseSpecificRule("enum", KotlinGrammar.modifiers)
        Assertions.assertNotNull(grammarNode)
        val modifiers = ArrayList<Modifier>()
        val annotation = fillModifiersAndReturnAnnotation(grammarNode!!, modifiers)
        Assertions.assertEquals("", annotation)
        Assertions.assertEquals(1, modifiers.size)
        Assertions.assertTrue(modifiers[0] is ClassModifier)
        Assertions.assertEquals(ClassModifierType.ENUM, (modifiers[0] as ClassModifier).classModifierType)
    }

    @Test
    fun doubleTest()
    {
        val grammarNode = KotlinGrammar.parseSpecificRule("enum public", KotlinGrammar.modifiers)
        Assertions.assertNotNull(grammarNode)
        val modifiers = ArrayList<Modifier>()
        val annotation = fillModifiersAndReturnAnnotation(grammarNode!!, modifiers)
        Assertions.assertEquals("", annotation)
        Assertions.assertEquals(2, modifiers.size)
        Assertions.assertTrue(modifiers[0] is ClassModifier)
        Assertions.assertEquals(ClassModifierType.ENUM, (modifiers[0] as ClassModifier).classModifierType)
        Assertions.assertTrue(modifiers[1] is VisibilityModifier)
        Assertions.assertEquals(VisibilityModifierType.PUBLIC,
                                (modifiers[1] as VisibilityModifier).visibilityModifierType)
    }
}