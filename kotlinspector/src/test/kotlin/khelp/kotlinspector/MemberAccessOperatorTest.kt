package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MemberAccessOperatorTest
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule(".", KotlinGrammar.memberAccessOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(MemberAccessOperator.CALL, MemberAccessOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("::", KotlinGrammar.memberAccessOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(MemberAccessOperator.METHOD_REFERENCE, MemberAccessOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("?.", KotlinGrammar.memberAccessOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(MemberAccessOperator.NULLABLE_CALL, MemberAccessOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.memberAccessOperator)
        Assertions.assertNull(grammarNode)
        Assertions.assertEquals(MemberAccessOperator.NONE, MemberAccessOperator.parse("something"))
    }
}