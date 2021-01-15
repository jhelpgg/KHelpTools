package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.modifier.ClassModifier
import khelp.kotlinspector.model.modifier.ClassModifierType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ClassModifierTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("enum", KotlinGrammar.classModifier)
        Assertions.assertNotNull(grammarNode)
        var classModifier = ClassModifier()
        classModifier.parse(grammarNode!!)
        Assertions.assertEquals(ClassModifierType.ENUM, classModifier.classModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("sealed", KotlinGrammar.classModifier)
        Assertions.assertNotNull(grammarNode)
        classModifier = ClassModifier()
        classModifier.parse(grammarNode!!)
        Assertions.assertEquals(ClassModifierType.SEALED, classModifier.classModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("annotation", KotlinGrammar.classModifier)
        Assertions.assertNotNull(grammarNode)
        classModifier = ClassModifier()
        classModifier.parse(grammarNode!!)
        Assertions.assertEquals(ClassModifierType.ANNOTATION, classModifier.classModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("inner", KotlinGrammar.classModifier)
        Assertions.assertNotNull(grammarNode)
        classModifier = ClassModifier()
        classModifier.parse(grammarNode!!)
        Assertions.assertEquals(ClassModifierType.INNER, classModifier.classModifierType)

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.classModifier)
        Assertions.assertNull(grammarNode)
    }
}