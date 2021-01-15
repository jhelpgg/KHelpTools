package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.AdditiveOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AdditiveOperatorTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("+", KotlinGrammar.additiveOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(AdditiveOperator.PLUS, AdditiveOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("-", KotlinGrammar.additiveOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(AdditiveOperator.MINUS, AdditiveOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.additiveOperator)
        Assertions.assertNull(grammarNode)
    }
}