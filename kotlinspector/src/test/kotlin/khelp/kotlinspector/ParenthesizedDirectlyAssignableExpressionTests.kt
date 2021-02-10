package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.ParenthesizedDirectlyAssignableExpression
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParenthesizedDirectlyAssignableExpressionTests
{
    @Test
    fun postSuffix()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("(listOf<Address>)",
                                                                        KotlinGrammar.parenthesizedDirectlyAssignableExpression))
        val parenthesizedDirectlyAssignableExpression = ParenthesizedDirectlyAssignableExpression()
        parenthesizedDirectlyAssignableExpression.parse(grammarNode)
        val postfixUnaryExpression = assumeNotNull(parenthesizedDirectlyAssignableExpression.directlyAssignableExpression.postfixUnaryExpression)
        Assertions.assertEquals("listOf", postfixUnaryExpression.primaryExpression.identifier)
        val typeArguments = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].typeArguments)
        val type = assumeNotNull(typeArguments.typeProjections()[0].type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Address", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun embed()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("((listOf<Address>))",
                                                                        KotlinGrammar.parenthesizedDirectlyAssignableExpression))
        var parenthesizedDirectlyAssignableExpression = ParenthesizedDirectlyAssignableExpression()
        parenthesizedDirectlyAssignableExpression.parse(grammarNode)
        parenthesizedDirectlyAssignableExpression = assumeNotNull(parenthesizedDirectlyAssignableExpression.directlyAssignableExpression.parenthesizedDirectlyAssignableExpression)
        val postfixUnaryExpression = assumeNotNull(parenthesizedDirectlyAssignableExpression.directlyAssignableExpression.postfixUnaryExpression)
        Assertions.assertEquals("listOf", postfixUnaryExpression.primaryExpression.identifier)
        val typeArguments = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].typeArguments)
        val type = assumeNotNull(typeArguments.typeProjections()[0].type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Address", userType.simpleUserTypes()[0].name)
    }
}