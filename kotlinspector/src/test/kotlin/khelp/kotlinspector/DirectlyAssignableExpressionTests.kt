package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.DirectlyAssignableExpression
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DirectlyAssignableExpressionTests
{
    @Test
    fun  postfixUnary()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("listOf<Address>",
                                                                             KotlinGrammar.directlyAssignableExpression))
        val directlyAssignableExpression = DirectlyAssignableExpression()
        directlyAssignableExpression.parse(grammarNode)
                val postfixUnaryExpression = assumeNotNull(directlyAssignableExpression.postfixUnaryExpression)
        Assertions.assertEquals("listOf", postfixUnaryExpression.primaryExpression.identifier)
        val typeArguments = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].typeArguments)
        val type = assumeNotNull(typeArguments.typeProjections()[0].type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Address", userType.simpleUserTypes()[0].name)
    }
    @Test
    fun postSuffixParenthesis()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("(listOf<Address>)",
                                                                             KotlinGrammar.directlyAssignableExpression))
        val directlyAssignableExpression = DirectlyAssignableExpression()
        directlyAssignableExpression.parse(grammarNode)
        val parenthesizedDirectlyAssignableExpression = assumeNotNull(directlyAssignableExpression.parenthesizedDirectlyAssignableExpression)
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
                                                                             KotlinGrammar.directlyAssignableExpression))
        val directlyAssignableExpression = DirectlyAssignableExpression()
        directlyAssignableExpression.parse(grammarNode)
        var parenthesizedDirectlyAssignableExpression = assumeNotNull(directlyAssignableExpression.parenthesizedDirectlyAssignableExpression)
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