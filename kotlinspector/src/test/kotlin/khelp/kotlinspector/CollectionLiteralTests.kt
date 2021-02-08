package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.CollectionLiteral
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.AdditiveOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CollectionLiteralTests
{
    @Test
    fun emptyCollection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[]",
                                                                        KotlinGrammar.collectionLiteral))
        val collectionLiteral = CollectionLiteral()
        collectionLiteral.parse(grammarNode)
        Assertions.assertTrue(collectionLiteral.expressions()
                                  .isEmpty())
    }

    @Test
    fun oneConstant()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[42]",
                                                                        KotlinGrammar.collectionLiteral))
        val collectionLiteral = CollectionLiteral()
        collectionLiteral.parse(grammarNode)
        Assertions.assertEquals(1, collectionLiteral.expressions().size)
        val expression = collectionLiteral.expressions()[0]
        val literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
    }

    @Test
    fun oneOperation()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[ 42 - 73 ]",
                                                                        KotlinGrammar.collectionLiteral))
        val collectionLiteral = CollectionLiteral()
        collectionLiteral.parse(grammarNode)
        Assertions.assertEquals(1, collectionLiteral.expressions().size)
        val expression = collectionLiteral.expressions()[0]
        val additiveExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0]
        var literalConstant = assumeNotNull(additiveExpression.firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        val (additiveOperator, multiplicativeExpression) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[0]
        Assertions.assertEquals(AdditiveOperator.MINUS, additiveOperator)
        literalConstant = assumeNotNull(multiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
    }

    @Test
    fun oneCall()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[ computeKey() ]",
                                                                        KotlinGrammar.collectionLiteral))
        val collectionLiteral = CollectionLiteral()
        collectionLiteral.parse(grammarNode)
        Assertions.assertEquals(1, collectionLiteral.expressions().size)
        val expression = collectionLiteral.expressions()[0]
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeKey", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun twoArguments()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[ \"key\", 666 ]",
                                                                        KotlinGrammar.collectionLiteral))
        val collectionLiteral = CollectionLiteral()
        collectionLiteral.parse(grammarNode)
        Assertions.assertEquals(2, collectionLiteral.expressions().size)

        var expression = collectionLiteral.expressions()[0]
        val stringLiteral = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("key", lineStrText.text)

        expression = collectionLiteral.expressions()[1]
        val literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("666", literalConstant.value)
    }

    @Test
    fun threeArguments() {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[ \"key\", 666, computeKey() ]",
                                                                        KotlinGrammar.collectionLiteral))
        val collectionLiteral = CollectionLiteral()
        collectionLiteral.parse(grammarNode)
        Assertions.assertEquals(3, collectionLiteral.expressions().size)

        var expression = collectionLiteral.expressions()[0]
        val stringLiteral = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("key", lineStrText.text)

        expression = collectionLiteral.expressions()[1]
        val literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("666", literalConstant.value)

        expression = collectionLiteral.expressions()[2]
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeKey", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }
}