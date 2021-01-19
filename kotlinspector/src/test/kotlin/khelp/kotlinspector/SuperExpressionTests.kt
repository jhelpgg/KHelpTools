package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.SuperExpression
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SuperExpressionTests
{
    @Test
    fun superOnly()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("super", KotlinGrammar.superExpression))
        val superExpression = SuperExpression()
        superExpression.parse(grammarNode)
        Assertions.assertTrue(superExpression.simpleIdentifier.isEmpty())
        Assertions.assertNull(superExpression.type)
    }

    @Test
    fun superType()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("super<String>", KotlinGrammar.superExpression))
        val superExpression = SuperExpression()
        superExpression.parse(grammarNode)
        Assertions.assertTrue(superExpression.simpleIdentifier.isEmpty())
        val type = assumeNotNull(superExpression.type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun superTypeAt()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("super<String>@method",
                                                                        KotlinGrammar.superExpression))
        val superExpression = SuperExpression()
        superExpression.parse(grammarNode)
        Assertions.assertEquals("method", superExpression.simpleIdentifier)
        val type = assumeNotNull(superExpression.type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun superAt()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("super@method", KotlinGrammar.superExpression))
        val superExpression = SuperExpression()
        superExpression.parse(grammarNode)
        Assertions.assertEquals("method", superExpression.simpleIdentifier)
        Assertions.assertNull(superExpression.type)
    }
}