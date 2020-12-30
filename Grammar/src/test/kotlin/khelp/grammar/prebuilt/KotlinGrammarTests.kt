package khelp.grammar.prebuilt

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class KotlinGrammarTests
{
    @Test
    fun simpleClassTest()
    {
        val grammarNode = KotlinGrammar.parse(KotlinGrammarTests::class.java.getResourceAsStream("SimpleClass.kt"))
        Assertions.assertNotNull(grammarNode)
        grammarNode!!.removeEmptyNodes()

        // Check header comments
        var child = grammarNode[0]
        Assertions.assertEquals(KotlinGrammar.LineComment, child[0][0].rule)
        Assertions.assertEquals("// One line comment before package\n", child[0][0].text)
        Assertions.assertEquals(KotlinGrammar.WS, child[1][0].rule)
        Assertions.assertEquals("\n", child[1][0].text)
        Assertions.assertEquals(KotlinGrammar.DelimitedComment, child[2][0].rule)
        Assertions.assertEquals("/*\n * Multiline comment before package\n */", child[2][0].text)
        Assertions.assertEquals(KotlinGrammar.WS, child[3][0].rule)
        Assertions.assertEquals("\n\n", child[3][0].text)

        // Check package
        child = grammarNode[1].searchByFirstDeep(KotlinGrammar.packageHeader)
                ?: Assertions.fail("Should have a packageHeader")
        var identifier = child.searchByFirstDeep(KotlinGrammar.identifier)
                         ?: Assertions.fail("Should have an identifier")
        Assertions.assertEquals("khelp.grammar.prebuilt", identifier.text)

        // Check imports
        val collector = ArrayList<String>()
        grammarNode.forEachDeep({ node -> node.rule == KotlinGrammar.importHeader }) { node ->
            node.searchByFirstDeep(KotlinGrammar.identifier)
                ?.let { nodeIdentifier -> collector.add(nodeIdentifier.text) }
        }
        Assertions.assertEquals(2, collector.size)
        Assertions.assertEquals("khelp.utilities.regex.NAME", collector[0])
        Assertions.assertEquals("khelp.utilities.text.interval", collector[1])

        // Check class
        child = grammarNode.searchByFirstDeep(KotlinGrammar.classDeclaration)
                ?: Assertions.fail("Should have a class declaration")
        identifier = child.searchByFirstHeap(KotlinGrammar.Identifier)
                     ?: Assertions.fail("Should have an identifier")
        Assertions.assertEquals("SimpleClass", identifier.text)

        // Check properties
        collector.clear()
        child.forEachDeep({ node -> node.rule == KotlinGrammar.propertyDeclaration }) { node ->
            node.searchByFirstHeap(KotlinGrammar.Identifier)
                ?.let { nodeIdentifier -> collector.add(nodeIdentifier.text) }
        }
        Assertions.assertEquals(1, collector.size)
        Assertions.assertEquals("interval", collector[0])


        // Check functions
        collector.clear()
        child.forEachDeep({ node -> node.rule == KotlinGrammar.functionDeclaration }) { node ->
            node.searchByFirstHeap(KotlinGrammar.Identifier)
                ?.let { nodeIdentifier -> collector.add(nodeIdentifier.text) }
        }
        Assertions.assertEquals(2, collector.size)
        Assertions.assertEquals("isValid", collector[0])
        Assertions.assertEquals("inside", collector[1])
    }
}
