package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.ElvisExpression
import khelp.kotlinspector.model.expression.JumpExpressionType
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ElvisExpressionTests
{
    @Test
    fun noElvis() {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("result",
        KotlinGrammar.elvisExpression))
        val elvisExpression = ElvisExpression()
        elvisExpression.parse(grammarNode)
        Assertions.assertEquals("result",elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }
    @Test
    fun justElvis() {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("result ?: return",
        KotlinGrammar.elvisExpression))
        val elvisExpression = ElvisExpression()
        elvisExpression.parse(grammarNode)
        Assertions.assertEquals("result",elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val jumpExpression = assumeNotNull(elvisExpression.infixFunctionCalls()[1].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.jumpExpression)
        Assertions.assertEquals(JumpExpressionType.RETURN, jumpExpression.jumpExpressionType)
    }

    @Test
    fun alternativeElvis() {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("result?.callMethod() ?: defaultValue",
        KotlinGrammar.elvisExpression))
        var elvisExpression = ElvisExpression()
        elvisExpression.parse(grammarNode)
        var postfixUnaryExpression = elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("result", postfixUnaryExpression.primaryExpression.identifier)
        val navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.NULLABLE_CALL, navigationSuffix.memberAccessOperator)
        val expression = assumeNotNull(navigationSuffix.expression)
        elvisExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression
        postfixUnaryExpression = elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("callMethod", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix= assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments().isEmpty())
        Assertions.assertEquals("defaultValue", elvisExpression.infixFunctionCalls()[1].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }
}