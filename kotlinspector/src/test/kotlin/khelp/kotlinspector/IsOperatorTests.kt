package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.IsOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IsOperatorTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("is", KotlinGrammar.isOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(IsOperator.IS, IsOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("!is", KotlinGrammar.isOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(IsOperator.NOT_IS, IsOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.isOperator)
        Assertions.assertNull(grammarNode)
    }
}