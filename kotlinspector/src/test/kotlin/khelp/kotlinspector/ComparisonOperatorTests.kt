package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.ComparisonOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ComparisonOperatorTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("<", KotlinGrammar.comparisonOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(ComparisonOperator.LOWER, ComparisonOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("<=", KotlinGrammar.comparisonOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(ComparisonOperator.LOWER_EQUAL, ComparisonOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule(">", KotlinGrammar.comparisonOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(ComparisonOperator.UPPER, ComparisonOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule(">=", KotlinGrammar.comparisonOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(ComparisonOperator.UPPER_EQUAL, ComparisonOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.comparisonOperator)
        Assertions.assertNull(grammarNode)
    }
}