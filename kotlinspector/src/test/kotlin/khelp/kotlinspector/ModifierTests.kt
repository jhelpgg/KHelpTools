package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.modifier.ClassModifier
import khelp.kotlinspector.model.modifier.ClassModifierType
import khelp.kotlinspector.model.modifier.FunctionModifier
import khelp.kotlinspector.model.modifier.FunctionModifierType
import khelp.kotlinspector.model.modifier.InheritanceModifier
import khelp.kotlinspector.model.modifier.InheritanceModifierType
import khelp.kotlinspector.model.modifier.MemberModifier
import khelp.kotlinspector.model.modifier.MemberModifierType
import khelp.kotlinspector.model.modifier.ParameterModifier
import khelp.kotlinspector.model.modifier.ParameterModifierType
import khelp.kotlinspector.model.modifier.PlatformModifier
import khelp.kotlinspector.model.modifier.PlatformModifierType
import khelp.kotlinspector.model.modifier.PropertyModifier
import khelp.kotlinspector.model.modifier.VisibilityModifier
import khelp.kotlinspector.model.modifier.VisibilityModifierType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ModifierTests
{
    @Test
    fun classModifierTest()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("enum", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        var modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is ClassModifier)
        Assertions.assertEquals(ClassModifierType.ENUM, (modifier as ClassModifier).classModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("sealed", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is ClassModifier)
        Assertions.assertEquals(ClassModifierType.SEALED, (modifier as ClassModifier).classModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("annotation", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is ClassModifier)
        Assertions.assertEquals(ClassModifierType.ANNOTATION, (modifier as ClassModifier).classModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("data", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is ClassModifier)
        Assertions.assertEquals(ClassModifierType.DATA, (modifier as ClassModifier).classModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("inner", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is ClassModifier)
        Assertions.assertEquals(ClassModifierType.INNER, (modifier as ClassModifier).classModifierType)
    }

    @Test
    fun memberModifierTest()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("override", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        var modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is MemberModifier)
        Assertions.assertEquals(MemberModifierType.OVERRIDE, (modifier as MemberModifier).memberModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("lateinit", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is MemberModifier)
        Assertions.assertEquals(MemberModifierType.LATEINIT, (modifier as MemberModifier).memberModifierType)
    }

    @Test
    fun visibilityModifierTest()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("public", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        var modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is VisibilityModifier)
        Assertions.assertEquals(VisibilityModifierType.PUBLIC, (modifier as VisibilityModifier).visibilityModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("private", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is VisibilityModifier)
        Assertions.assertEquals(VisibilityModifierType.PRIVATE, (modifier as VisibilityModifier).visibilityModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("internal", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is VisibilityModifier)
        Assertions.assertEquals(VisibilityModifierType.INTERNAL,
                                (modifier as VisibilityModifier).visibilityModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("protected", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is VisibilityModifier)
        Assertions.assertEquals(VisibilityModifierType.PROTECTED,
                                (modifier as VisibilityModifier).visibilityModifierType)
    }

    @Test
    fun functionModifierTest()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("tailrec", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        var modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is FunctionModifier)
        Assertions.assertEquals(FunctionModifierType.TAILREC, (modifier as FunctionModifier).functionModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("operator", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is FunctionModifier)
        Assertions.assertEquals(FunctionModifierType.OPERATOR, (modifier as FunctionModifier).functionModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("infix", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is FunctionModifier)
        Assertions.assertEquals(FunctionModifierType.INFIX, (modifier as FunctionModifier).functionModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("inline", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is FunctionModifier)
        Assertions.assertEquals(FunctionModifierType.INLINE, (modifier as FunctionModifier).functionModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("external", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is FunctionModifier)
        Assertions.assertEquals(FunctionModifierType.EXTERNAL, (modifier as FunctionModifier).functionModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("suspend", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is FunctionModifier)
        Assertions.assertEquals(FunctionModifierType.SUSPEND, (modifier as FunctionModifier).functionModifierType)
    }

    @Test
    fun propertyModifierTest()
    {
        val grammarNode = KotlinGrammar.parseSpecificRule("const", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        val modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is PropertyModifier)
    }

    @Test
    fun inheritanceModifierTest()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("abstract", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        var modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is InheritanceModifier)
        Assertions.assertEquals(InheritanceModifierType.ABSTRACT,
                                (modifier as InheritanceModifier).inheritanceModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("final", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is InheritanceModifier)
        Assertions.assertEquals(InheritanceModifierType.FINAL,
                                (modifier as InheritanceModifier).inheritanceModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("open", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is InheritanceModifier)
        Assertions.assertEquals(InheritanceModifierType.OPEN, (modifier as InheritanceModifier).inheritanceModifierType)
    }

    @Test
    fun parameterModifierTest()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("crossinline", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        var modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is ParameterModifier)
        Assertions.assertEquals(ParameterModifierType.CROSSINLINE,
                                (modifier as ParameterModifier).parameterModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("noinline", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is ParameterModifier)
        Assertions.assertEquals(ParameterModifierType.NOINLINE, (modifier as ParameterModifier).parameterModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("vararg", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is ParameterModifier)
        Assertions.assertEquals(ParameterModifierType.VARARG, (modifier as ParameterModifier).parameterModifierType)
    }

    @Test
    fun platformModifierTest()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("actual", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        var modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is PlatformModifier)
        Assertions.assertEquals(PlatformModifierType.ACTUAL, (modifier as PlatformModifier).platformModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("expect", KotlinGrammar.modifier)
        Assertions.assertNotNull(grammarNode)
        modifier = parseModifierInformation(grammarNode!!)
        Assertions.assertTrue(modifier is PlatformModifier)
        Assertions.assertEquals(PlatformModifierType.EXPECT, (modifier as PlatformModifier).platformModifierType)
    }
}