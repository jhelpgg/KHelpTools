package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.`when`.WhenCondition
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.AdditiveOperator
import khelp.kotlinspector.model.expression.operator.ComparisonOperator
import khelp.kotlinspector.model.expression.operator.InOperator
import khelp.kotlinspector.model.expression.operator.IsOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class WhenConditionTests
{
    @Test
    fun expression()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("value < first + second",
                                                                        KotlinGrammar.whenCondition))
        val whenCondition = WhenCondition()
        whenCondition.parse(grammarNode)
        val expression = assumeNotNull(whenCondition.expression)
        val firstComparison = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison
        var postfixUnaryExpression = firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("value", postfixUnaryExpression.primaryExpression.identifier)
        val (operator, comparison) = firstComparison.operatorGenericCallLikeComparisonCouples()[0]
        Assertions.assertEquals(ComparisonOperator.LOWER, operator)
        val additiveExpression = comparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0]
        postfixUnaryExpression = additiveExpression.firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("first", postfixUnaryExpression.primaryExpression.identifier)
        val (additiveOperator, multiplicativeExpression) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[0]
        Assertions.assertEquals(AdditiveOperator.PLUS, additiveOperator)
        postfixUnaryExpression = multiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("second", postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun rangeTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("in 3 until 9", KotlinGrammar.whenCondition))
        val whenCondition = WhenCondition()
        whenCondition.parse(grammarNode)
        val rangeTest = assumeNotNull(whenCondition.rangeTest)
        Assertions.assertEquals(InOperator.IN, rangeTest.inOperator)
        val infixFunctionCall = rangeTest.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0]
        var literalConstant = assumeNotNull(infixFunctionCall.firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("3", literalConstant.value.trim())
        val (name,rangeExpression) = infixFunctionCall.nameRangeExpressionCouples()[0]
        Assertions.assertEquals("until", name)
        literalConstant = assumeNotNull(rangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("9", literalConstant.value)
    }

    @Test
    fun typeType()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("is List<Int>", KotlinGrammar.whenCondition))
        val whenCondition = WhenCondition()
        whenCondition.parse(grammarNode)
        val typeTest = assumeNotNull(whenCondition.typeTest)
        Assertions.assertEquals(IsOperator.IS, typeTest.isOperator)
        var typeReference = assumeNotNull(typeTest.type.typeReference)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("List", userType.simpleUserTypes()[0].name)
        val typeArguments = assumeNotNull(userType.simpleUserTypes()[0].typeArguments)
        val type = assumeNotNull(typeArguments.typeProjections()[0].type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Int", userType.simpleUserTypes()[0].name)
    }
}