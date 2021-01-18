package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.IsOperator
import khelp.kotlinspector.model.expression.test.TypeTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TypeTestTests
{
    @Test
    fun test()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("is Cancelable", KotlinGrammar.typeTest))
        var typeTest = TypeTest()
        typeTest.parse(grammarNode)
        Assertions.assertEquals(IsOperator.IS, typeTest.isOperator)
        var typeReference = assumeNotNull(typeTest.type.typeReference)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Cancelable", userType.simpleUserTypes()[0].name)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("!is String", KotlinGrammar.typeTest))
        typeTest = TypeTest()
        typeTest.parse(grammarNode)
        Assertions.assertEquals(IsOperator.NOT_IS, typeTest.isOperator)
        typeReference = assumeNotNull(typeTest.type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
    }
}