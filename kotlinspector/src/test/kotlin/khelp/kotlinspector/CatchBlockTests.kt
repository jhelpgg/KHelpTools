package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator
import khelp.kotlinspector.model.expression.trycacth.CatchBlock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CatchBlockTests
{
    @Test
    fun test()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            catch(ioException : IOException) {
                this.alertReadingIssue(ioException)
            }
        """.trimIndent(), KotlinGrammar.catchBlock))
        val catchBlock = CatchBlock()
        catchBlock.parse(grammarNode)

        Assertions.assertTrue(catchBlock.annotation.isEmpty())

        Assertions.assertEquals("ioException", catchBlock.simpleIdentifier)

        Assertions.assertNull(catchBlock.type.parenthesizedType)
        Assertions.assertNull(catchBlock.type.functionType)
        Assertions.assertNull(catchBlock.type.nullableType)
        val typeReference = assumeNotNull(catchBlock.type.typeReference)
        Assertions.assertFalse(typeReference.isDynamic)
        val userType = assumeNotNull(typeReference.userType)
        val simpleUserTypes = userType.simpleUserTypes()
        Assertions.assertEquals(1, simpleUserTypes.size)
        Assertions.assertEquals("IOException", simpleUserTypes[0].name)

        val statements = catchBlock.block.statements()
        Assertions.assertEquals(1, statements.size)
        val statement = statements[0]
        Assertions.assertTrue(statement.annotation.isEmpty())
        Assertions.assertTrue(statement.label.isEmpty())
        Assertions.assertNull(statement.assignment)
        Assertions.assertNull(statement.declaration)
        Assertions.assertNull(statement.loopStatement)
        var expression = assumeNotNull(statement.expression)
        var postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression

        val thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())

        val postfixUnarySuffixs = postfixUnaryExpression.postfixUnarySuffixs()
        Assertions.assertEquals(1, postfixUnarySuffixs.size)
        val navigationSuffix = assumeNotNull(postfixUnarySuffixs[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("alertReadingIssue", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val valueArguments = callSuffix.valueArguments()
        Assertions.assertEquals(1, valueArguments.size)
        postfixUnaryExpression = valueArguments[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("ioException", postfixUnaryExpression.primaryExpression.identifier)
    }
}