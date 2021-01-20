package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.declaration.FunctionDeclaration
import khelp.kotlinspector.model.delegation.UserType
import khelp.kotlinspector.model.expression.literal.ObjectLiteral
import khelp.kotlinspector.model.modifier.MemberModifier
import khelp.kotlinspector.model.modifier.MemberModifierType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ObjectLiteralTests
{
    @Test
    fun delegatedObject()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            object : Runnable {
                override fun run() 
                {
                    doSomething()
                }
            }
        """.trimIndent(), KotlinGrammar.objectLiteral))
        val objectLiteral = ObjectLiteral()
        objectLiteral.parse(grammarNode)
        val userType: UserType = assumeIs(objectLiteral.delegationSpecifiers()[0].delegationSpecifier)
        Assertions.assertEquals("Runnable", userType.simpleUserTypes()[0].name)
        val functionDeclaration: FunctionDeclaration = assumeIs(objectLiteral.classBody.classMemberDeclarations()[0].declaration)
        val memberModifier: MemberModifier = assumeIs(functionDeclaration.modifiers()[0])
        Assertions.assertEquals(MemberModifierType.OVERRIDE, memberModifier.memberModifierType)
        Assertions.assertEquals("run", functionDeclaration.name)
        Assertions.assertTrue(functionDeclaration.functionValueParameters()
                                  .isEmpty())
        val functionBody = assumeNotNull(functionDeclaration.functionBody)
        val block = assumeNotNull(functionBody.block)
        val expression = assumeNotNull(block.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("doSomething",
                                postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun notDelegatedObject()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            object {
                fun method() 
                {
                    doSomething()
                }
            }
        """.trimIndent(), KotlinGrammar.objectLiteral))
        val objectLiteral = ObjectLiteral()
        objectLiteral.parse(grammarNode)
        Assertions.assertTrue(objectLiteral.delegationSpecifiers()
                                  .isEmpty())
        val functionDeclaration: FunctionDeclaration = assumeIs(objectLiteral.classBody.classMemberDeclarations()[0].declaration)
        Assertions.assertTrue(functionDeclaration.modifiers()
                                  .isEmpty())
        Assertions.assertEquals("method", functionDeclaration.name)
        Assertions.assertTrue(functionDeclaration.functionValueParameters()
                                  .isEmpty())
        val functionBody = assumeNotNull(functionDeclaration.functionBody)
        val block = assumeNotNull(functionBody.block)
        val expression = assumeNotNull(block.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("doSomething",
                                postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }
}