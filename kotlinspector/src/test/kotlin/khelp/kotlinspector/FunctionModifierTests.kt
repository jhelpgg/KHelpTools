package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.modifier.FunctionModifier
import khelp.kotlinspector.model.modifier.FunctionModifierType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FunctionModifierTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("tailrec", KotlinGrammar.functionModifier)
        Assertions.assertNotNull(grammarNode)
        var functionModifier = FunctionModifier()
        functionModifier.parse(grammarNode!!)
        Assertions.assertEquals(FunctionModifierType.TAILREC, functionModifier.functionModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("operator", KotlinGrammar.functionModifier)
        Assertions.assertNotNull(grammarNode)
        functionModifier = FunctionModifier()
        functionModifier.parse(grammarNode!!)
        Assertions.assertEquals(FunctionModifierType.OPERATOR, functionModifier.functionModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("infix", KotlinGrammar.functionModifier)
        Assertions.assertNotNull(grammarNode)
        functionModifier = FunctionModifier()
        functionModifier.parse(grammarNode!!)
        Assertions.assertEquals(FunctionModifierType.INFIX, functionModifier.functionModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("inline", KotlinGrammar.functionModifier)
        Assertions.assertNotNull(grammarNode)
        functionModifier = FunctionModifier()
        functionModifier.parse(grammarNode!!)
        Assertions.assertEquals(FunctionModifierType.INLINE, functionModifier.functionModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("external", KotlinGrammar.functionModifier)
        Assertions.assertNotNull(grammarNode)
        functionModifier = FunctionModifier()
        functionModifier.parse(grammarNode!!)
        Assertions.assertEquals(FunctionModifierType.EXTERNAL, functionModifier.functionModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("suspend", KotlinGrammar.functionModifier)
        Assertions.assertNotNull(grammarNode)
        functionModifier = FunctionModifier()
        functionModifier.parse(grammarNode!!)
        Assertions.assertEquals(FunctionModifierType.SUSPEND, functionModifier.functionModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.functionModifier)
        Assertions.assertNull(grammarNode)
    }
}