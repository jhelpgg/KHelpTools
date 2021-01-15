package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.CallableReference
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CallableReferenceTests
{
    @Test
    fun typedIdentifierReference()
    {
        val grammarNode = KotlinGrammar.parseSpecificRule("CallableReferenceTests::identifierReference",
                                                          KotlinGrammar.callableReference)
        Assertions.assertNotNull(grammarNode)
        val callableReference = CallableReference()
        callableReference.parse(grammarNode!!)

        Assertions.assertFalse(callableReference.isClasReference)

        val receiverType = assumeNotNull(callableReference.receiverType)
        val typeReference = assumeNotNull(receiverType.typeReference)
        Assertions.assertFalse(typeReference.isDynamic)
        val userType = assumeNotNull(typeReference.userType)
        val simpleUserTypes = userType.simpleUserTypes()
        Assertions.assertEquals(1, simpleUserTypes.size)
        val simpleUserType = simpleUserTypes[0]
        Assertions.assertNull(simpleUserType.typeArguments)
        Assertions.assertEquals("CallableReferenceTests", simpleUserType.name)

        Assertions.assertEquals("identifierReference", callableReference.simpleIdentifier)
    }

    @Test
    fun identifierReference()
    {
        val grammarNode = KotlinGrammar.parseSpecificRule("::identifierReference", KotlinGrammar.callableReference)
        Assertions.assertNotNull(grammarNode)
        val callableReference = CallableReference()
        callableReference.parse(grammarNode!!)

        Assertions.assertFalse(callableReference.isClasReference)

        Assertions.assertNull(callableReference.receiverType)

        Assertions.assertEquals("identifierReference", callableReference.simpleIdentifier)
    }

    @Test
    fun classReference()
    {
        val grammarNode = KotlinGrammar.parseSpecificRule("CallableReferenceTests::class",
                                                          KotlinGrammar.callableReference)
        Assertions.assertNotNull(grammarNode)
        val callableReference = CallableReference()
        callableReference.parse(grammarNode!!)

        Assertions.assertTrue(callableReference.isClasReference)

        val receiverType = assumeNotNull(callableReference.receiverType)
        val typeReference = assumeNotNull(receiverType.typeReference)
        Assertions.assertFalse(typeReference.isDynamic)
        val userType = assumeNotNull(typeReference.userType)
        val simpleUserTypes = userType.simpleUserTypes()
        Assertions.assertEquals(1, simpleUserTypes.size)
        val simpleUserType = simpleUserTypes[0]
        Assertions.assertNull(simpleUserType.typeArguments)
        Assertions.assertEquals("CallableReferenceTests", simpleUserType.name)

        Assertions.assertEquals("", callableReference.simpleIdentifier)
    }
}