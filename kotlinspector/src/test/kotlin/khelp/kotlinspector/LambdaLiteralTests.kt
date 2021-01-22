package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.literal.lambda.LambdaLiteral
import khelp.kotlinspector.model.expression.operator.AdditiveOperator
import khelp.kotlinspector.model.expression.operator.AssignmentAndOperator
import khelp.kotlinspector.model.expression.operator.MultiplicativeOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LambdaLiteralTests
{
    @Test
    fun lambdaNoParameter()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            { sum += it }
        """.trimIndent(), KotlinGrammar.lambdaLiteral))
        val lambdaLiteral = LambdaLiteral()
        lambdaLiteral.parse(grammarNode)
        Assertions.assertTrue(lambdaLiteral.lambdaParameters()
                                  .isEmpty())
        val assignment = assumeNotNull(lambdaLiteral.statements()[0].assignment)
        Assertions.assertEquals(AssignmentAndOperator.PLUS_ASSIGN, assignment.assignmentAndOperator)
        val assignableExpression = assumeNotNull(assignment.assignableExpression)
        val prefixUnaryExpression = assumeNotNull(assignableExpression.prefixUnaryExpression)
        Assertions.assertEquals("sum", prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        Assertions.assertEquals("it",
                                assignment.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun lambdaOneParameter()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            { value : Int -> value + 42 }
        """.trimIndent(), KotlinGrammar.lambdaLiteral))
        val lambdaLiteral = LambdaLiteral()
        lambdaLiteral.parse(grammarNode)
        val variableDeclaration = lambdaLiteral.lambdaParameters()[0].variableDeclarations()[0]
        Assertions.assertEquals("value", variableDeclaration.name)
        val type = assumeNotNull(variableDeclaration.type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Int", userType.simpleUserTypes()[0].name)
        val expression = assumeNotNull(lambdaLiteral.statements()[0].expression)
        val additiveExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0]
        Assertions.assertEquals("value",
                                additiveExpression.firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (additiveOperator, multiplicativeExpression) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[0]
        Assertions.assertEquals(AdditiveOperator.PLUS, additiveOperator)
        val literalConstant = assumeNotNull(multiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
    }

    @Test
    fun lambdaTwoParameters()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
           { first, second ->
                first * second
           }
        """.trimIndent(), KotlinGrammar.lambdaLiteral))
        val lambdaLiteral = LambdaLiteral()
        lambdaLiteral.parse(grammarNode)
        Assertions.assertEquals("first", lambdaLiteral.lambdaParameters()[0].variableDeclarations()[0].name)
        Assertions.assertEquals("second", lambdaLiteral.lambdaParameters()[1].variableDeclarations()[0].name)
        val expression = assumeNotNull(lambdaLiteral.statements()[0].expression)
        val firstMultiplicativeExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression
        Assertions.assertEquals("first",
                                firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (multiplicativeOperator, asExpression) = firstMultiplicativeExpression.multiplicativeOperatorAsExpressionCouples()[0]
        Assertions.assertEquals(MultiplicativeOperator.TIMES, multiplicativeOperator)
        Assertions.assertEquals("second",
                                asExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }
}