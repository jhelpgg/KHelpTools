package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.PrefixUnaryOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PrefixUnaryOperatorTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("++", KotlinGrammar.prefixUnaryOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(PrefixUnaryOperator.INCREMENT, PrefixUnaryOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("--", KotlinGrammar.prefixUnaryOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(PrefixUnaryOperator.DECREMENT, PrefixUnaryOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("!", KotlinGrammar.prefixUnaryOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(PrefixUnaryOperator.NOT, PrefixUnaryOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.prefixUnaryOperator)
        Assertions.assertNull(grammarNode)
        Assertions.assertEquals(PrefixUnaryOperator.NONE, PrefixUnaryOperator.parse("something"))
    }
}