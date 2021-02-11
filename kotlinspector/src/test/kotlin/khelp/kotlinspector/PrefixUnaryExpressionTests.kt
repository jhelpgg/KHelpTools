package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.PrefixUnaryExpression
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.PrefixUnaryOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PrefixUnaryExpressionTests
{
    @Test
    fun invokeMethod()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("compute(42, 73)",
                                                                        KotlinGrammar.prefixUnaryExpression))
        val prefixUnaryExpression = PrefixUnaryExpression()
        prefixUnaryExpression.parse(grammarNode)
        val postfixUnaryExpression = prefixUnaryExpression.postfixUnaryExpression
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
    fun oppositeInvokeMethod()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("-compute(42, 73)",
                                                                        KotlinGrammar.prefixUnaryExpression))
        val prefixUnaryExpression = PrefixUnaryExpression()
        prefixUnaryExpression.parse(grammarNode)
        Assertions.assertEquals(PrefixUnaryOperator.UNARY_MINUS,
                                prefixUnaryExpression.unaryPrefixs()[0].prefixUnaryOperator)
        val postfixUnaryExpression = prefixUnaryExpression.postfixUnaryExpression
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
    fun incrementValue()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("++index",
                                                                        KotlinGrammar.prefixUnaryExpression))
        val prefixUnaryExpression = PrefixUnaryExpression()
        prefixUnaryExpression.parse(grammarNode)
        Assertions.assertEquals(PrefixUnaryOperator.INCREMENT,
                                prefixUnaryExpression.unaryPrefixs()[0].prefixUnaryOperator)
        val postfixUnaryExpression = prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("index", postfixUnaryExpression.primaryExpression.identifier)
    }
}