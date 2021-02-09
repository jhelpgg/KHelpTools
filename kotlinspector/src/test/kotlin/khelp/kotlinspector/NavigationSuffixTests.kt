package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.NavigationSuffix
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class NavigationSuffixTests
{
    @Test
    fun callMethod()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule(".indexOf(data)",
                                                                        KotlinGrammar.navigationSuffix))
        val navigationSuffix = NavigationSuffix()
        navigationSuffix.parse(grammarNode)
        Assertions.assertFalse(navigationSuffix.isClass)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        val expression = assumeNotNull(navigationSuffix.expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("indexOf", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals("data",
                                callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun nullableCallMethod()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("?.indexOf(data)",
                                                                        KotlinGrammar.navigationSuffix))
        val navigationSuffix = NavigationSuffix()
        navigationSuffix.parse(grammarNode)
        Assertions.assertFalse(navigationSuffix.isClass)
        Assertions.assertEquals(MemberAccessOperator.NULLABLE_CALL, navigationSuffix.memberAccessOperator)
        val expression = assumeNotNull(navigationSuffix.expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("indexOf", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals("data",
                                callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun referenceMethod()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("::indexOf",
                                                                        KotlinGrammar.navigationSuffix))
        val navigationSuffix = NavigationSuffix()
        navigationSuffix.parse(grammarNode)
        Assertions.assertFalse(navigationSuffix.isClass)
        Assertions.assertEquals(MemberAccessOperator.METHOD_REFERENCE, navigationSuffix.memberAccessOperator)
        val expression = assumeNotNull(navigationSuffix.expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("indexOf", postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun classReference()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("::class",
                                                                        KotlinGrammar.navigationSuffix))
        val navigationSuffix = NavigationSuffix()
        navigationSuffix.parse(grammarNode)
        Assertions.assertTrue(navigationSuffix.isClass)
        Assertions.assertEquals(MemberAccessOperator.METHOD_REFERENCE, navigationSuffix.memberAccessOperator)
        Assertions.assertNull(navigationSuffix.expression)
    }
}
