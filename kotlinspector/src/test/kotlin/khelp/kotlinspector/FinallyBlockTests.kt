package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator
import khelp.kotlinspector.model.expression.trycacth.FinallyBlock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FinallyBlockTests
{
    @Test
    fun test()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            finally {
                inputStream?.close()
            }
        """.trimIndent(), KotlinGrammar.finallyBlock))
        val finallyBlock = FinallyBlock()
        finallyBlock.parse(grammarNode)
        val statements = finallyBlock.block.statements()
        Assertions.assertEquals(1, statements.size)
        val statement = statements[0]
        Assertions.assertTrue(statement.annotation.isEmpty())
        Assertions.assertTrue(statement.label.isEmpty())
        Assertions.assertNull(statement.assignment)
        Assertions.assertNull(statement.declaration)
        Assertions.assertNull(statement.loopStatement)
        var expression = assumeNotNull(statement.expression)
        var postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("inputStream", postfixUnaryExpression.primaryExpression.identifier)
        Assertions.assertEquals(1, postfixUnaryExpression.postfixUnarySuffixs().size)
        val postfixUnarySuffix = postfixUnaryExpression.postfixUnarySuffixs()[0]
        val navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.NULLABLE_CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("close", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }
}