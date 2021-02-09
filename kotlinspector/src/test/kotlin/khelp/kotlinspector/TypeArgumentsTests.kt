package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.TypeArguments
import khelp.kotlinspector.model.typeparameter.VarianceModifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TypeArgumentsTests
{
    @Test
    fun oneStarTypeProjection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<*>",
                                                                        KotlinGrammar.typeArguments))
        val typeArguments = TypeArguments()
        typeArguments.parse(grammarNode)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(1, typeProjections.size)
        val typeProjection = typeProjections[0]
        Assertions.assertTrue(typeProjection.isStar)
        Assertions.assertNull(typeProjection.type)
        Assertions.assertTrue(typeProjection.typeProjectionModifiers()
                                  .isEmpty())
    }

    @Test
    fun oneSimpleTypeProjection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<String>",
                                                                        KotlinGrammar.typeArguments))
        val typeArguments = TypeArguments()
        typeArguments.parse(grammarNode)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(1, typeProjections.size)
        val typeProjection = typeProjections[0]
        Assertions.assertFalse(typeProjection.isStar)
        Assertions.assertTrue(typeProjection.typeProjectionModifiers()
                                  .isEmpty())
        val type = assumeNotNull(typeProjection.type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun inTypeProjection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<in InputStream>",
                                                                        KotlinGrammar.typeArguments))
        val typeArguments = TypeArguments()
        typeArguments.parse(grammarNode)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(1, typeProjections.size)
        val typeProjection = typeProjections[0]
        Assertions.assertFalse(typeProjection.isStar)
        val typeProjectionModifiers = typeProjection.typeProjectionModifiers()
        Assertions.assertEquals(1, typeProjectionModifiers.size)
        Assertions.assertEquals(VarianceModifier.IN, typeProjectionModifiers[0].varianceModifier)
        val type = assumeNotNull(typeProjection.type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("InputStream", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun severalTypes()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<String, Int, in InputStream, out OutputStream>",
                                                                        KotlinGrammar.typeArguments))
        val typeArguments = TypeArguments()
        typeArguments.parse(grammarNode)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(4, typeProjections.size)

        var typeProjection = typeProjections[0]
        Assertions.assertFalse(typeProjection.isStar)
        var typeProjectionModifiers = typeProjection.typeProjectionModifiers()
        Assertions.assertTrue(typeProjectionModifiers.isEmpty())
        var type = assumeNotNull(typeProjection.type)
        var typeReference = assumeNotNull(type.typeReference)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)

        typeProjection = typeProjections[1]
        Assertions.assertFalse(typeProjection.isStar)
        typeProjectionModifiers = typeProjection.typeProjectionModifiers()
        Assertions.assertTrue(typeProjectionModifiers.isEmpty())
        type = assumeNotNull(typeProjection.type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Int", userType.simpleUserTypes()[0].name)

        typeProjection = typeProjections[2]
        Assertions.assertFalse(typeProjection.isStar)
        typeProjectionModifiers = typeProjection.typeProjectionModifiers()
        Assertions.assertEquals(1, typeProjectionModifiers.size)
        Assertions.assertEquals(VarianceModifier.IN, typeProjectionModifiers[0].varianceModifier)
        type = assumeNotNull(typeProjection.type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("InputStream", userType.simpleUserTypes()[0].name)

        typeProjection = typeProjections[3]
        Assertions.assertFalse(typeProjection.isStar)
        typeProjectionModifiers = typeProjection.typeProjectionModifiers()
        Assertions.assertEquals(1, typeProjectionModifiers.size)
        Assertions.assertEquals(VarianceModifier.OUT, typeProjectionModifiers[0].varianceModifier)
        type = assumeNotNull(typeProjection.type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("OutputStream", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun typeWithProjection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<Pair<String, Person>>",
                                                                        KotlinGrammar.typeArguments))
        var typeArguments = TypeArguments()
        typeArguments.parse(grammarNode)
        var typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(1, typeProjections.size)
        var type = assumeNotNull(typeProjections[0].type)
        var typeReference = assumeNotNull(type.typeReference)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Pair", userType.simpleUserTypes()[0].name)

        typeArguments = assumeNotNull(userType.simpleUserTypes()[0].typeArguments)
        typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(2, typeProjections.size)
        type = assumeNotNull(typeProjections[0].type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)

        type = assumeNotNull(typeProjections[1].type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Person", userType.simpleUserTypes()[0].name)
    }
}