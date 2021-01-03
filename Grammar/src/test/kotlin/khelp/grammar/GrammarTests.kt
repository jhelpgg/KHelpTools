package khelp.grammar

import khelp.utilities.extensions.regularExpression
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GrammarTests
{
    @Test
    fun cyclicTest()
    {
        val grammar = Grammar()
        grammar.rules {
            "A" IS { rule = ('a'.regularExpression * "A") I 'a'.regularExpression }
        }

        // EMPTY
        var grammarNode = grammar.parse("")
        Assertions.assertNull(grammarNode)

        // a
        grammarNode = grammar.parse("a")
        Assertions.assertNotNull(grammarNode)
        println(grammarNode)
        Assertions.assertEquals("A", grammarNode!!.rule)
        Assertions.assertEquals("a", grammarNode.text)
        Assertions.assertEquals(1, grammarNode.numberChildren)
        grammarNode = grammarNode[0]
        Assertions.assertEquals("A/1", grammarNode.rule)
        Assertions.assertEquals("a", grammarNode.text)
        Assertions.assertEquals(0, grammarNode.numberChildren)

        // aa
        grammarNode = grammar.parse("aa")
        Assertions.assertNotNull(grammarNode)
        println(grammarNode)
        Assertions.assertEquals("A", grammarNode!!.rule)
        Assertions.assertEquals("aa", grammarNode.text)
        Assertions.assertEquals(1, grammarNode.numberChildren)
        grammarNode = grammarNode[0]
        Assertions.assertEquals("A/0", grammarNode.rule)
        Assertions.assertEquals("aa", grammarNode.text)
        Assertions.assertEquals(2, grammarNode.numberChildren)
        var grammarNodeSub = grammarNode[0]
        Assertions.assertEquals("A/0@0", grammarNodeSub.rule)
        Assertions.assertEquals("a", grammarNodeSub.text)
        Assertions.assertEquals(0, grammarNodeSub.numberChildren)
        grammarNodeSub = grammarNode[1]
        Assertions.assertEquals("A", grammarNodeSub.rule)
        Assertions.assertEquals("a", grammarNodeSub.text)
        Assertions.assertEquals(1, grammarNodeSub.numberChildren)
        val grammarNodeSubSub = grammarNodeSub[0]
        Assertions.assertEquals("A/1", grammarNodeSubSub.rule)
        Assertions.assertEquals("a", grammarNodeSubSub.text)
        Assertions.assertEquals(0, grammarNodeSubSub.numberChildren)

        // aab
        grammarNode = grammar.parse("aab")
        Assertions.assertNull(grammarNode)
    }
}