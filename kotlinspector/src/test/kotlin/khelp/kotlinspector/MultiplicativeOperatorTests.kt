package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.MultiplicativeOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MultiplicativeOperatorTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("*", KotlinGrammar.multiplicativeOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(MultiplicativeOperator.TIMES, MultiplicativeOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("/", KotlinGrammar.multiplicativeOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(MultiplicativeOperator.DIVIDE, MultiplicativeOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("%", KotlinGrammar.multiplicativeOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(MultiplicativeOperator.REMAINDER, MultiplicativeOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.multiplicativeOperator)
        Assertions.assertNull(grammarNode)
    }
}