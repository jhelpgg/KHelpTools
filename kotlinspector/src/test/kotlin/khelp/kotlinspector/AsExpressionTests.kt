package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.AsExpression
import khelp.kotlinspector.model.expression.JumpExpressionType
import khelp.kotlinspector.model.expression.operator.AsOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AsExpressionTests
{
    @Test
    fun prefixUnaryExpression()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("return index",
                                                                        KotlinGrammar.asExpression))
        val asExpression = AsExpression()
        asExpression.parse(grammarNode)
        val jumpExpression = assumeNotNull(asExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.jumpExpression)
        Assertions.assertEquals(JumpExpressionType.RETURN, jumpExpression.jumpExpressionType)
        val expression = assumeNotNull(jumpExpression.expression)
        Assertions.assertEquals("index",
                                expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun simpleAs()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("engine as Car",
                                                                        KotlinGrammar.asExpression))
        val asExpression = AsExpression()
        asExpression.parse(grammarNode)
        Assertions.assertEquals("engine",
                                asExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (asOperator, type) = asExpression.asOperatorTypeCouples()[0]
        Assertions.assertEquals(AsOperator.AS, asOperator)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Car", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun asNullable()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("engine as? Car",
                                                                        KotlinGrammar.asExpression))
        val asExpression = AsExpression()
        asExpression.parse(grammarNode)
        Assertions.assertEquals("engine",
                                asExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (asOperator, type) = asExpression.asOperatorTypeCouples()[0]
        Assertions.assertEquals(AsOperator.AS_NULLABLE, asOperator)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Car", userType.simpleUserTypes()[0].name)
    }
}