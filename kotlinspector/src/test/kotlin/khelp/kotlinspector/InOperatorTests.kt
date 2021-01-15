package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.InOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class InOperatorTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("in", KotlinGrammar.inOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(InOperator.IN, InOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("!in", KotlinGrammar.inOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(InOperator.NOT_IN, InOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.inOperator)
        Assertions.assertNull(grammarNode)
    }
}