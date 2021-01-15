package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.typeparameter.VarianceModifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class VarianceModifierTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("in", KotlinGrammar.varianceModifier)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(VarianceModifier.IN, VarianceModifier.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("out", KotlinGrammar.varianceModifier)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(VarianceModifier.OUT, VarianceModifier.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.varianceModifier)
        Assertions.assertNull(grammarNode)
        Assertions.assertEquals(VarianceModifier.NONE, VarianceModifier.parse("something"))
    }
}