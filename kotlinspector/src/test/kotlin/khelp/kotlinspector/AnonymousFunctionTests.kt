package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.AnonymousFunction
import khelp.kotlinspector.model.expression.JumpExpressionType
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.AdditiveOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AnonymousFunctionTests
{
    @Test
    fun anonymousFunctionSimple()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            fun  ()  
            {
                doSomething()
            }
        """.trimIndent(), KotlinGrammar.anonymousFunction))
        val anonymousFunction = AnonymousFunction()
        anonymousFunction.parse(grammarNode)
        val functionBody = assumeNotNull(anonymousFunction.functionBody)
        val block = assumeNotNull(functionBody.block)
        val expression = assumeNotNull(block.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("doSomething", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun anonymousFunctionTyped()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            fun String.(value, text:String) {
                  doSomething(text+" : "+value)
            }
        """.trimIndent(), KotlinGrammar.anonymousFunction))
        val anonymousFunction = AnonymousFunction()
        anonymousFunction.parse(grammarNode)

        var typeReference = assumeNotNull(anonymousFunction.parametersTypes()[0].typeReference)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)

        var parameterWithOptionalType = anonymousFunction.parametersWithOptionalType()[0]
        Assertions.assertEquals("value", parameterWithOptionalType.name)
        Assertions.assertNull(parameterWithOptionalType.type)

        parameterWithOptionalType = anonymousFunction.parametersWithOptionalType()[1]
        Assertions.assertEquals("text", parameterWithOptionalType.name)
        val type = assumeNotNull(parameterWithOptionalType.type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)

        val functionBody = assumeNotNull(anonymousFunction.functionBody)
        val block = assumeNotNull(functionBody.block)
        val expression = assumeNotNull(block.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("doSomething", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val additiveExpression = callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0]
        Assertions.assertEquals("text",
                                additiveExpression.firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (additiveOperator0, multiplicativeExpression0) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[0]
        Assertions.assertEquals(AdditiveOperator.PLUS, additiveOperator0)
        val stringLiteral = assumeNotNull(multiplicativeExpression0.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals(" : ", lineStrText.text)
        val (additiveOperator1, multiplicativeExpression1) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[1]
        Assertions.assertEquals(AdditiveOperator.PLUS, additiveOperator1)
        Assertions.assertEquals("value",
                                multiplicativeExpression1.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun anonymousFunctionDelegated()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            fun (value) : Int {
                  doSomething(value)
                  return 73
            }
        """.trimIndent(), KotlinGrammar.anonymousFunction))
        val anonymousFunction = AnonymousFunction()
        anonymousFunction.parse(grammarNode)

        val parameterWithOptionalType = anonymousFunction.parametersWithOptionalType()[0]
        Assertions.assertEquals("value", parameterWithOptionalType.name)
        Assertions.assertNull(parameterWithOptionalType.type)

        val returnType = assumeNotNull(anonymousFunction.returnType)
        val typeReference = assumeNotNull(returnType.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Int", userType.simpleUserTypes()[0].name)

        val functionBody = assumeNotNull(anonymousFunction.functionBody)
        val block = assumeNotNull(functionBody.block)
        var expression = assumeNotNull(block.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("doSomething", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals("value",
                                callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        expression = assumeNotNull(block.statements()[1].expression)
        val jumpExpression = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.jumpExpression)
        Assertions.assertEquals(JumpExpressionType.RETURN, jumpExpression.jumpExpressionType)
        expression = assumeNotNull(jumpExpression.expression)
        val literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
    }
}