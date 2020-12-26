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
            "A" IS { rule = 'a'.regularExpression I ('a'.regularExpression * "A") }
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
        Assertions.assertEquals(1, grammarNode.children.size)
        grammarNode = grammarNode.children[0]
        Assertions.assertEquals("A*", grammarNode.rule)
        Assertions.assertEquals("a", grammarNode.text)
        Assertions.assertEquals(0, grammarNode.children.size)

        // aa
        grammarNode = grammar.parse("aa")
        Assertions.assertNotNull(grammarNode)
        println(grammarNode)
        Assertions.assertEquals("A", grammarNode!!.rule)
        Assertions.assertEquals("aa", grammarNode.text)
        Assertions.assertEquals(1, grammarNode.children.size)
        grammarNode = grammarNode.children[0]
        Assertions.assertEquals("A*", grammarNode.rule)
        Assertions.assertEquals("aa", grammarNode.text)
        Assertions.assertEquals(2, grammarNode.children.size)
        var grammarNodeSub = grammarNode.children[0]
        Assertions.assertEquals("A*@0", grammarNodeSub.rule)
        Assertions.assertEquals("a", grammarNodeSub.text)
        Assertions.assertEquals(0, grammarNodeSub.children.size)
        grammarNodeSub = grammarNode.children[1]
        Assertions.assertEquals("A", grammarNodeSub.rule)
        Assertions.assertEquals("a", grammarNodeSub.text)
        Assertions.assertEquals(1, grammarNodeSub.children.size)
        val grammarNodeSubSub = grammarNodeSub.children[0]
        Assertions.assertEquals("A*", grammarNodeSubSub.rule)
        Assertions.assertEquals("a", grammarNodeSubSub.text)
        Assertions.assertEquals(0, grammarNodeSubSub.children.size)

        // aab
        grammarNode = grammar.parse("aab")
        Assertions.assertNull(grammarNode)
    }
}