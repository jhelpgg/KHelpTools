package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.`when`.WhenExpression
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.ComparisonOperator
import khelp.kotlinspector.model.expression.operator.IsOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class WhenExpressionTests
{
    @Test
    fun withSubjectTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            when(value) {
                is Number -> 0b1000_0001
                42        -> 0b0001_1000
                else      -> 0b1010_0101
            }
        """.trimIndent(), KotlinGrammar.whenExpression))
        val whenExpression = WhenExpression()
        whenExpression.parse(grammarNode)
        val whenSubject = assumeNotNull(whenExpression.whenSubject)
        Assertions.assertEquals("value",
                                whenSubject.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)

        var whenEntry = whenExpression.whenEntries()[0]
        val typeTest = assumeNotNull(whenEntry.whenConditions()[0].typeTest)
        Assertions.assertEquals(IsOperator.IS, typeTest.isOperator)
        val typeReference = assumeNotNull(typeTest.type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Number", userType.simpleUserTypes()[0].name)
        var statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        var expression = assumeNotNull(statement.expression)
        var literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1000_0001", literalConstant.value)

        whenEntry = whenExpression.whenEntries()[1]
        expression = assumeNotNull(whenEntry.whenConditions()[0].expression)
        literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        expression = assumeNotNull(statement.expression)
        literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b0001_1000", literalConstant.value)

        whenEntry = whenExpression.whenEntries()[2]
        Assertions.assertTrue(whenEntry.whenConditions()
                                  .isEmpty())
        statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        expression = assumeNotNull(statement.expression)
        literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1010_0101", literalConstant.value)
    }

    @Test
    fun withoutSubjectTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            when {
                value > 73 -> 0b1000_0001
                value < 42 -> 0b0001_1000
                else       -> 0b1010_0101
            }
        """.trimIndent(), KotlinGrammar.whenExpression))
        val whenExpression = WhenExpression()
        whenExpression.parse(grammarNode)
        Assertions.assertNull(whenExpression.whenSubject)

        var whenEntry = whenExpression.whenEntries()[0]
        var expression = assumeNotNull(whenEntry.whenConditions()[0].expression)
        var equality = expression.disjunction.conjunctions()[0].equalities()[0]
        Assertions.assertEquals("value",
                                equality.firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (operator, comparison) = equality.firstComparison.operatorGenericCallLikeComparisonCouples()[0]
        Assertions.assertEquals(ComparisonOperator.UPPER, operator)
        var literalConstant = assumeNotNull(comparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
        var statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        expression = assumeNotNull(statement.expression)
        literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1000_0001", literalConstant.value)

        whenEntry = whenExpression.whenEntries()[1]
        expression = assumeNotNull(whenEntry.whenConditions()[0].expression)
        equality = expression.disjunction.conjunctions()[0].equalities()[0]
        Assertions.assertEquals("value",
                                equality.firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (operator2, comparison2) = equality.firstComparison.operatorGenericCallLikeComparisonCouples()[0]
        Assertions.assertEquals(ComparisonOperator.LOWER, operator2)
        literalConstant = assumeNotNull(comparison2.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        expression = assumeNotNull(statement.expression)
        literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b0001_1000", literalConstant.value)

        whenEntry = whenExpression.whenEntries()[2]
        Assertions.assertTrue(whenEntry.whenConditions()
                                  .isEmpty())
        statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        expression = assumeNotNull(statement.expression)
        literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1010_0101", literalConstant.value)
    }
}