package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.lambda.LambdaParameter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LambdaParameterTests
{
    @Test
    fun oneVariable()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("parameter",
                                                                        KotlinGrammar.lambdaParameter))
        val lambdaParameter = LambdaParameter()
        lambdaParameter.parse(grammarNode)
        Assertions.assertNull(lambdaParameter.type)
        val variableDeclarations = lambdaParameter.variableDeclarations()
        Assertions.assertEquals(1, variableDeclarations.size)
        val variableDeclaration = variableDeclarations[0]
        Assertions.assertEquals("parameter", variableDeclaration.name)
        Assertions.assertNull(variableDeclaration.type)
        Assertions.assertTrue(variableDeclaration.annotation.isEmpty())
    }

    @Test
    fun oneVariableTyped()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("parameter:String",
                                                                        KotlinGrammar.lambdaParameter))
        val lambdaParameter = LambdaParameter()
        lambdaParameter.parse(grammarNode)
        Assertions.assertNull(lambdaParameter.type)
        val variableDeclarations = lambdaParameter.variableDeclarations()
        Assertions.assertEquals(1, variableDeclarations.size)
        val variableDeclaration = variableDeclarations[0]
        Assertions.assertEquals("parameter", variableDeclaration.name)
        Assertions.assertTrue(variableDeclaration.annotation.isEmpty())
        val type = assumeNotNull(variableDeclaration.type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun twoVariables()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("(parameter1, parameter2)",
                                                                        KotlinGrammar.lambdaParameter))
        val lambdaParameter = LambdaParameter()
        lambdaParameter.parse(grammarNode)
        Assertions.assertNull(lambdaParameter.type)
        val variableDeclarations = lambdaParameter.variableDeclarations()
        Assertions.assertEquals(2, variableDeclarations.size)
        var variableDeclaration = variableDeclarations[0]
        Assertions.assertEquals("parameter1", variableDeclaration.name)
        Assertions.assertNull(variableDeclaration.type)
        Assertions.assertTrue(variableDeclaration.annotation.isEmpty())
        variableDeclaration = variableDeclarations[1]
        Assertions.assertEquals("parameter2", variableDeclaration.name)
        Assertions.assertNull(variableDeclaration.type)
        Assertions.assertTrue(variableDeclaration.annotation.isEmpty())
    }

    @Test
    fun twoVariablesTyped()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("(parameter1, parameter2) : Pair<String, Int>",
                                                                        KotlinGrammar.lambdaParameter))
        val lambdaParameter = LambdaParameter()
        lambdaParameter.parse(grammarNode)
        var type = assumeNotNull(lambdaParameter.type)
        var typeReference = assumeNotNull(type.typeReference)
        var userType = assumeNotNull(typeReference.userType)
        var simpleUserType = userType.simpleUserTypes()[0]
        Assertions.assertEquals("Pair", simpleUserType.name)
        val typeArguments = assumeNotNull(simpleUserType.typeArguments)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(2, typeProjections.size)
        type = assumeNotNull(typeProjections[0].type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        simpleUserType = userType.simpleUserTypes()[0]
        Assertions.assertEquals("String", simpleUserType.name)
        type = assumeNotNull(typeProjections[1].type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        simpleUserType = userType.simpleUserTypes()[0]
        Assertions.assertEquals("Int", simpleUserType.name)
        val variableDeclarations = lambdaParameter.variableDeclarations()
        Assertions.assertEquals(2, variableDeclarations.size)
        var variableDeclaration = variableDeclarations[0]
        Assertions.assertEquals("parameter1", variableDeclaration.name)
        Assertions.assertNull(variableDeclaration.type)
        Assertions.assertTrue(variableDeclaration.annotation.isEmpty())
        variableDeclaration = variableDeclarations[1]
        Assertions.assertEquals("parameter2", variableDeclaration.name)
        Assertions.assertNull(variableDeclaration.type)
        Assertions.assertTrue(variableDeclaration.annotation.isEmpty())
    }
}