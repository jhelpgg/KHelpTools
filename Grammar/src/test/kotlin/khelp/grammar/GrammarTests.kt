package khelp.grammar

import khelp.utilities.extensions.regularExpression
import org.junit.Assert
import org.junit.Test

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
        Assert.assertNull(grammarNode)

        // a
        grammarNode = grammar.parse("a")
        Assert.assertNotNull(grammarNode)
        println(grammarNode)
        Assert.assertEquals("A", grammarNode!!.rule)
        Assert.assertEquals("a", grammarNode.text)
        Assert.assertEquals(1, grammarNode.children.size)
        grammarNode = grammarNode.children[0]
        Assert.assertEquals("A*", grammarNode.rule)
        Assert.assertEquals("a", grammarNode.text)
        Assert.assertEquals(0, grammarNode.children.size)

        // aa
        grammarNode = grammar.parse("aa")
        Assert.assertNotNull(grammarNode)
        println(grammarNode)
        Assert.assertEquals("A", grammarNode!!.rule)
        Assert.assertEquals("aa", grammarNode.text)
        Assert.assertEquals(1, grammarNode.children.size)
        grammarNode = grammarNode.children[0]
        Assert.assertEquals("A*", grammarNode.rule)
        Assert.assertEquals("aa", grammarNode.text)
        Assert.assertEquals(2, grammarNode.children.size)
        var grammarNodeSub = grammarNode.children[0]
        Assert.assertEquals("A*@0", grammarNodeSub.rule)
        Assert.assertEquals("a", grammarNodeSub.text)
        Assert.assertEquals(0, grammarNodeSub.children.size)
        grammarNodeSub = grammarNode.children[1]
        Assert.assertEquals("A", grammarNodeSub.rule)
        Assert.assertEquals("a", grammarNodeSub.text)
        Assert.assertEquals(1, grammarNodeSub.children.size)
        val grammarNodeSubSub = grammarNodeSub.children[0]
        Assert.assertEquals("A*", grammarNodeSubSub.rule)
        Assert.assertEquals("a", grammarNodeSubSub.text)
        Assert.assertEquals(0, grammarNodeSubSub.children.size)

        // aab
        grammarNode = grammar.parse("aab")
        Assert.assertNull(grammarNode)
    }
}