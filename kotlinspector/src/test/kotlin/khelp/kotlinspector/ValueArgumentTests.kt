package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.ValueArgument
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ValueArgumentTests
{
    @Test
    fun simpleAffection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("index = 42",
                                                                        KotlinGrammar.valueArgument))
        val valueArgument = ValueArgument()
        valueArgument.parse(grammarNode)
        Assertions.assertEquals("", valueArgument.annotation)
        Assertions.assertEquals("index", valueArgument.name)
        Assertions.assertFalse(valueArgument.asStar)
        val literalConstant = assumeNotNull(valueArgument.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
    }

    @Test
    fun starAffection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("arguments = *array",
                                                                        KotlinGrammar.valueArgument))
        val valueArgument = ValueArgument()
        valueArgument.parse(grammarNode)
        Assertions.assertEquals("", valueArgument.annotation)
        Assertions.assertEquals("arguments", valueArgument.name)
        Assertions.assertTrue(valueArgument.asStar)
        Assertions.assertEquals("array", valueArgument.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun callAffection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("index = computeIndex()",
                                                                        KotlinGrammar.valueArgument))
        val valueArgument = ValueArgument()
        valueArgument.parse(grammarNode)
        Assertions.assertEquals("", valueArgument.annotation)
        Assertions.assertEquals("index", valueArgument.name)
        Assertions.assertFalse(valueArgument.asStar)
        val postfixUnaryExpression = valueArgument.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeIndex",postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments().isEmpty())
    }

    @Test
    fun annotatedSimpleAffection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("@NotNull index = 42",
                                                                        KotlinGrammar.valueArgument))
        val valueArgument = ValueArgument()
        valueArgument.parse(grammarNode)
        Assertions.assertEquals("@NotNull", valueArgument.annotation)
        Assertions.assertEquals("index", valueArgument.name)
        Assertions.assertFalse(valueArgument.asStar)
        val literalConstant = assumeNotNull(valueArgument.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
    }

    @Test
    fun annotatedStarAffection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("@NotNull arguments = *array",
                                                                        KotlinGrammar.valueArgument))
        val valueArgument = ValueArgument()
        valueArgument.parse(grammarNode)
        Assertions.assertEquals("@NotNull", valueArgument.annotation)
        Assertions.assertEquals("arguments", valueArgument.name)
        Assertions.assertTrue(valueArgument.asStar)
        Assertions.assertEquals("array", valueArgument.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun annotatedCallAffection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("@NotNull index = computeIndex()",
                                                                        KotlinGrammar.valueArgument))
        val valueArgument = ValueArgument()
        valueArgument.parse(grammarNode)
        Assertions.assertEquals("@NotNull", valueArgument.annotation)
        Assertions.assertEquals("index", valueArgument.name)
        Assertions.assertFalse(valueArgument.asStar)
        val postfixUnaryExpression = valueArgument.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeIndex",postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments().isEmpty())
    }
}