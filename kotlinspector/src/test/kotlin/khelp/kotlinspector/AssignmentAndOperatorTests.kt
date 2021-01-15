package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.AssignmentAndOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AssignmentAndOperatorTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("+=", KotlinGrammar.assignmentAndOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(AssignmentAndOperator.PLUS_ASSIGN, AssignmentAndOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("-=", KotlinGrammar.assignmentAndOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(AssignmentAndOperator.MINUS_ASSIGN, AssignmentAndOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("*=", KotlinGrammar.assignmentAndOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(AssignmentAndOperator.TIMES_ASSIGN, AssignmentAndOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("/=", KotlinGrammar.assignmentAndOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(AssignmentAndOperator.DIVIDE_ASSIGN, AssignmentAndOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("%=", KotlinGrammar.assignmentAndOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(AssignmentAndOperator.REMAINDER_ASSIGN, AssignmentAndOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.assignmentAndOperator)
        Assertions.assertNull(grammarNode)
    }
}