package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.PostfixUnaryOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PostfixUnaryOperatorTests
{
    @Test
    fun test()
    {
        var grammarNode = KotlinGrammar.parseSpecificRule("++", KotlinGrammar.postfixUnaryOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(PostfixUnaryOperator.INCREMENT, PostfixUnaryOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("--", KotlinGrammar.postfixUnaryOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(PostfixUnaryOperator.DECREMENT, PostfixUnaryOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("!!", KotlinGrammar.postfixUnaryOperator)
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(PostfixUnaryOperator.ASSUME_NOT_NULL, PostfixUnaryOperator.parse(grammarNode!!.text))

        grammarNode = KotlinGrammar.parseSpecificRule("something", KotlinGrammar.postfixUnaryOperator)
        Assertions.assertNull(grammarNode)
        Assertions.assertEquals(PostfixUnaryOperator.NONE, PostfixUnaryOperator.parse("something"))
    }
}