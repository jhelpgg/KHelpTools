package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.GenericCallLikeComparison
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GenericCallLikeComparisonTests
{
    @Test
    fun metaMetaMetaMethod()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("metaMetaMetaMethod(42)(\"Test\")(true)(index)",
                                                                        KotlinGrammar.genericCallLikeComparison))
        val genericCallLikeComparison = GenericCallLikeComparison()
        genericCallLikeComparison.parse(grammarNode)
        val postfixUnaryExpression = genericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("metaMetaMetaMethod", postfixUnaryExpression.primaryExpression.identifier)
        var callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        var literalConstant = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[1].callSuffix)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Test", lineStrText.text)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[2].callSuffix)
        literalConstant = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BOOLEAN, literalConstant.literalConstantType)
        Assertions.assertEquals("true", literalConstant.value)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[3].callSuffix)
        Assertions.assertEquals("index",
                                callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }
}