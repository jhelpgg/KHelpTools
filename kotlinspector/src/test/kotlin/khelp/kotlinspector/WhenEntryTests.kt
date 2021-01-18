package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.JumpExpressionType
import khelp.kotlinspector.model.expression.`when`.WhenEntry
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.InOperator
import khelp.kotlinspector.model.expression.operator.IsOperator
import khelp.kotlinspector.model.expression.operator.MultiplicativeOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class WhenEntryTests
{
    @Test
    fun simpleExpressionEntry()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("ANSWER -> 42", KotlinGrammar.whenEntry))
        val whenEntry = WhenEntry()
        whenEntry.parse(grammarNode)
        var expression = assumeNotNull(whenEntry.whenConditions()[0].expression)
        Assertions.assertEquals("ANSWER",
                                expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        expression = assumeNotNull(statement.expression)
        val literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
    }

    @Test
    fun severalConditions()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("is Number, in list,  first * second -> { return \"Found !\" }",
                                                                        KotlinGrammar.whenEntry))
        val whenEntry = WhenEntry()
        whenEntry.parse(grammarNode)

        val typeTest = assumeNotNull(whenEntry.whenConditions()[0].typeTest)
        Assertions.assertEquals(IsOperator.IS, typeTest.isOperator)
        val typeReference = assumeNotNull(typeTest.type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Number", userType.simpleUserTypes()[0].name)

        val rangeTest = assumeNotNull(whenEntry.whenConditions()[1].rangeTest)
        Assertions.assertEquals(InOperator.IN, rangeTest.inOperator)
        Assertions.assertEquals("list",
                                rangeTest.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)

        var expression = assumeNotNull(whenEntry.whenConditions()[2].expression)
        val multiplicativeExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression
        Assertions.assertEquals("first",
                                multiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (multiplicativeOperator, asExpression) = multiplicativeExpression.multiplicativeOperatorAsExpressionCouples()[0]
        Assertions.assertEquals(MultiplicativeOperator.TIMES, multiplicativeOperator)
        Assertions.assertEquals("second",
                                asExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)

        val block = assumeNotNull(whenEntry.controlStructureBody.block)
        expression = assumeNotNull(block.statements()[0].expression)
        val jumpExpression = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.jumpExpression)
        Assertions.assertEquals(JumpExpressionType.RETURN, jumpExpression.jumpExpressionType)
        expression = assumeNotNull(jumpExpression.expression)
        val stringLiteral = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Found !", lineStrText.text)
    }

    @Test
    fun elseEntry()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("else -> '\\uCAFE'", KotlinGrammar.whenEntry))
        val whenEntry = WhenEntry()
        whenEntry.parse(grammarNode)
        Assertions.assertTrue(whenEntry.whenConditions()
                                  .isEmpty())
        val statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        val expression = assumeNotNull(statement.expression)
        val literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.CHARACTER, literalConstant.literalConstantType)
        Assertions.assertEquals("'\\uCAFE'", literalConstant.value)
    }
}