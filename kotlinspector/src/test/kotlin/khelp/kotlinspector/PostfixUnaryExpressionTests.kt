package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.PostfixUnaryExpression
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PostfixUnaryExpressionTests
{
    @Test
    fun primaryExpression()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("73.42",
                                                                        KotlinGrammar.postfixUnaryExpression))
        val postfixUnaryExpression = PostfixUnaryExpression()
        postfixUnaryExpression.parse(grammarNode)
        val literalConstant = assumeNotNull(postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.REAL, literalConstant.literalConstantType)
        Assertions.assertEquals("73.42", literalConstant.value)
    }

    @Test
    fun invokeMethod()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("compute(42, 73)",
                                                                        KotlinGrammar.postfixUnaryExpression))
        val postfixUnaryExpression = PostfixUnaryExpression()
        postfixUnaryExpression.parse(grammarNode)
        Assertions.assertEquals("compute", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        var literalConstant = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        literalConstant = assumeNotNull(callSuffix.valueArguments()[1].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
    }

    @Test
    fun typedInvokeMethod()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("execute<String>(\"Test\")",
                                                                        KotlinGrammar.postfixUnaryExpression))
        val postfixUnaryExpression = PostfixUnaryExpression()
        postfixUnaryExpression.parse(grammarNode)
        Assertions.assertEquals("execute", postfixUnaryExpression.primaryExpression.identifier)
         val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val typeArguments = assumeNotNull(callSuffix.typeArguments)
        val type = assumeNotNull(typeArguments.typeProjections()[0].type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Test", lineStrText.text)
    }
}