package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.ThisExpression
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ThisExpressionTests
{
    @Test
    fun thisOnly()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("this", KotlinGrammar.thisExpression))
        val thisExpression = ThisExpression()
        thisExpression.parse(grammarNode)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
    }

    @Test
    fun thisAt()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("this@method", KotlinGrammar.thisExpression))
        val thisExpression = ThisExpression()
        thisExpression.parse(grammarNode)
        Assertions.assertEquals("method", thisExpression.atIdentifier)
    }
}