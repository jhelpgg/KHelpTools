package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.AsOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AsOperatorTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("as", KotlinGrammar.asOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(AsOperator.AS, AsOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("as?", KotlinGrammar.asOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(AsOperator.AS_NULLABLE, AsOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.asOperator)
        Assertions.assertNull(grammarNode)
    }
}