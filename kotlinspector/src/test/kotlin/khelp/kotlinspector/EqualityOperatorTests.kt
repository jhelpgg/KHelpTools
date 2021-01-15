package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.EqualityOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EqualityOperatorTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("!=", KotlinGrammar.equalityOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(EqualityOperator.NOT_EQUALS, EqualityOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("!==", KotlinGrammar.equalityOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(EqualityOperator.NOT_SAME, EqualityOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("==", KotlinGrammar.equalityOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(EqualityOperator.EQUALS, EqualityOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("===", KotlinGrammar.equalityOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(EqualityOperator.SAME, EqualityOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.equalityOperator)
        Assertions.assertNull(grammarNode)
    }
}