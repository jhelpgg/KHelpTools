package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.CallSuffix
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.AssignmentAndOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CallSuffixTests
{
    @Test
    fun onlyValueArguments()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("(73, \"Magic number\")",
                                                                        KotlinGrammar.callSuffix))
        val callSuffix = CallSuffix()
        callSuffix.parse(grammarNode)
        val valueArguments = callSuffix.valueArguments()
        Assertions.assertEquals(2, valueArguments.size)
        val literalConstant = assumeNotNull(valueArguments[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
        val stringLiteral = assumeNotNull(valueArguments[1].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Magic number", lineStrText.text)
    }

    @Test
    fun onlyLambda()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("{ sum += it }",
                                                                        KotlinGrammar.callSuffix))
        val callSuffix = CallSuffix()
        callSuffix.parse(grammarNode)
        val annotatedLambda = assumeNotNull(callSuffix.annotatedLambda)
        val assignment = assumeNotNull(annotatedLambda.lambdaLiteral.statements()[0].assignment)
        val assignableExpression = assumeNotNull(assignment.assignableExpression)
        val prefixUnaryExpression = assumeNotNull(assignableExpression.prefixUnaryExpression)
        Assertions.assertEquals("sum", prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        Assertions.assertEquals(AssignmentAndOperator.PLUS_ASSIGN, assignment.assignmentAndOperator)
        Assertions.assertEquals("it",
                                assignment.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun typesArguments()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<Int, String> (73, \"Magic number\")",
                                                                        KotlinGrammar.callSuffix))
        val callSuffix = CallSuffix()
        callSuffix.parse(grammarNode)
        val typeArguments = assumeNotNull(callSuffix.typeArguments)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(2, typeProjections.size)
        var type = assumeNotNull(typeProjections[0].type)
        var typeReference = assumeNotNull(type.typeReference)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Int", userType.simpleUserTypes()[0].name)
        type = assumeNotNull(typeProjections[1].type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
        val valueArguments = callSuffix.valueArguments()
        Assertions.assertEquals(2, valueArguments.size)
        val literalConstant = assumeNotNull(valueArguments[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
        val stringLiteral = assumeNotNull(valueArguments[1].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Magic number", lineStrText.text)
    }

    @Test
    fun typesArgumentsAndLambda()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<Int, String, ()->Unit> (73, \"Magic number\") { println(\"Ok\") }",
                                                                        KotlinGrammar.callSuffix))
        var callSuffix = CallSuffix()
        callSuffix.parse(grammarNode)
        val typeArguments = assumeNotNull(callSuffix.typeArguments)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(3, typeProjections.size)
        var type = assumeNotNull(typeProjections[0].type)
        var typeReference = assumeNotNull(type.typeReference)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Int", userType.simpleUserTypes()[0].name)
        type = assumeNotNull(typeProjections[1].type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
        type = assumeNotNull(typeProjections[2].type)
        val functionType = assumeNotNull(type.functionType)
        Assertions.assertTrue(functionType.functionTypeParameters.typeOrParameters()
                                  .isEmpty())
        typeReference = assumeNotNull(functionType.type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Unit", userType.simpleUserTypes()[0].name)
        val valueArguments = callSuffix.valueArguments()
        Assertions.assertEquals(2, valueArguments.size)
        val literalConstant = assumeNotNull(valueArguments[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
        var stringLiteral = assumeNotNull(valueArguments[1].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        var lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        var lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        var lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Magic number", lineStrText.text)
        val annotatedLambda = assumeNotNull(callSuffix.annotatedLambda)
        val expression = assumeNotNull(annotatedLambda.lambdaLiteral.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("println", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Ok", lineStrText.text)
    }
}