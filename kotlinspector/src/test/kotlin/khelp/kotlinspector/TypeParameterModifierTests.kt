package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.typeparameter.TypeParameterModifier
import khelp.kotlinspector.model.typeparameter.VarianceModifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TypeParameterModifierTests
{
    @Test
    fun reificationModifierTest()
    {
        val grammarNode = KotlinGrammar.parseSpecificRule("reified", KotlinGrammar.typeParameterModifier)
        Assertions.assertNotNull(grammarNode)
        val typeParameterModifier = TypeParameterModifier()
        typeParameterModifier.parse(grammarNode!!)
        Assertions.assertTrue(typeParameterModifier.isReified)
        Assertions.assertTrue(typeParameterModifier.annotation.isEmpty())
        Assertions.assertEquals(VarianceModifier.NONE, typeParameterModifier.variance)
    }

    @Test
    fun varianceModifierTest()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("in", KotlinGrammar.typeParameterModifier)
        Assertions.assertNotNull(grammarNode)
        var typeParameterModifier = TypeParameterModifier()
        typeParameterModifier.parse(grammarNode!!)
        Assertions.assertFalse(typeParameterModifier.isReified)
        Assertions.assertTrue(typeParameterModifier.annotation.isEmpty())
        Assertions.assertEquals(VarianceModifier.IN, typeParameterModifier.variance)

        grammarNode = KotlinGrammar.parseSpecificRule("out", KotlinGrammar.typeParameterModifier)
        Assertions.assertNotNull(grammarNode)
        typeParameterModifier = TypeParameterModifier()
        typeParameterModifier.parse(grammarNode!!)
        Assertions.assertFalse(typeParameterModifier.isReified)
        Assertions.assertTrue(typeParameterModifier.annotation.isEmpty())
        Assertions.assertEquals(VarianceModifier.OUT, typeParameterModifier.variance)
    }

    @Test
    fun annotationTest()
    {
        val grammarNode = KotlinGrammar.parseSpecificRule("@Test", KotlinGrammar.typeParameterModifier)
        Assertions.assertNotNull(grammarNode)
        val typeParameterModifier = TypeParameterModifier()
        typeParameterModifier.parse(grammarNode!!)
        Assertions.assertFalse(typeParameterModifier.isReified)
        Assertions.assertEquals("@Test", typeParameterModifier.annotation)
        Assertions.assertEquals(VarianceModifier.NONE, typeParameterModifier.variance)
    }
}