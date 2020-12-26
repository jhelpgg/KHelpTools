package khelp.grammar.properties

import org.junit.Assert
import org.junit.Test

class PropertiesTest
{
    @Test
    fun propertiesTest()
    {
        val properties = Properties()
        val grammarNode = properties.parse(PropertiesTest::class.java.getResourceAsStream("PropertiesTest.properties"))
        Assert.assertNotNull(grammarNode)
        Assert.assertEquals(PROPERTIES_RULE_PROPERTIES, grammarNode!!.rule)
        Assert.assertEquals("""
            # ***************************************
            # *** Test of properties file parsing ***
            # ***************************************

            firstKey = First value
            secondKey = Second value

            # A comment

            thirdKey = Third value

        """.trimIndent(), grammarNode.text)
        Assert.assertEquals(10, grammarNode.children.size)

        // Line 1
        var nodeLine = grammarNode.children[0]
        Assert.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assert.assertEquals("# ***************************************\n", nodeLine.text)
        Assert.assertEquals(3, nodeLine.children.size)
        var nodeWhiteSpace = nodeLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assert.assertEquals("", nodeWhiteSpace.text)
        var nodePropertyLine = nodeLine.children[1]
        Assert.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assert.assertEquals("# ***************************************", nodePropertyLine.text)
        Assert.assertEquals(1, nodePropertyLine.children.size)
        var nodePropertyLineChoice = nodePropertyLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_COMMENT, nodePropertyLineChoice.rule)
        Assert.assertEquals("# ***************************************", nodePropertyLineChoice.text)
        var nodeEndLine = nodeLine.children[2]
        Assert.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assert.assertEquals("\n", nodeEndLine.text)

        // Line 2
        nodeLine = grammarNode.children[1]
        Assert.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assert.assertEquals("# *** Test of properties file parsing ***\n", nodeLine.text)
        Assert.assertEquals(3, nodeLine.children.size)
        nodeWhiteSpace = nodeLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assert.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine.children[1]
        Assert.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assert.assertEquals("# *** Test of properties file parsing ***", nodePropertyLine.text)
        Assert.assertEquals(1, nodePropertyLine.children.size)
        nodePropertyLineChoice = nodePropertyLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_COMMENT, nodePropertyLineChoice.rule)
        Assert.assertEquals("# *** Test of properties file parsing ***", nodePropertyLineChoice.text)
        nodeEndLine = nodeLine.children[2]
        Assert.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assert.assertEquals("\n", nodeEndLine.text)

        // Line 3
        nodeLine = grammarNode.children[2]
        Assert.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assert.assertEquals("# ***************************************\n", nodeLine.text)
        Assert.assertEquals(3, nodeLine.children.size)
        nodeWhiteSpace = nodeLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assert.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine.children[1]
        Assert.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assert.assertEquals("# ***************************************", nodePropertyLine.text)
        Assert.assertEquals(1, nodePropertyLine.children.size)
        nodePropertyLineChoice = nodePropertyLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_COMMENT, nodePropertyLineChoice.rule)
        Assert.assertEquals("# ***************************************", nodePropertyLineChoice.text)
        nodeEndLine = nodeLine.children[2]
        Assert.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assert.assertEquals("\n", nodeEndLine.text)

        // Line 4
        nodeLine = grammarNode.children[3]
        Assert.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assert.assertEquals("\n", nodeLine.text)
        Assert.assertEquals(3, nodeLine.children.size)
        nodeWhiteSpace = nodeLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assert.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine.children[1]
        Assert.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assert.assertEquals("", nodePropertyLine.text)
        Assert.assertEquals(1, nodePropertyLine.children.size)
        nodePropertyLineChoice = nodePropertyLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodePropertyLineChoice.rule)
        Assert.assertEquals("", nodePropertyLineChoice.text)
        nodeEndLine = nodeLine.children[2]
        Assert.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assert.assertEquals("\n", nodeEndLine.text)

        // Line 5
        nodeLine = grammarNode.children[4]
        Assert.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assert.assertEquals("firstKey = First value\n", nodeLine.text)
        Assert.assertEquals(3, nodeLine.children.size)
        nodeWhiteSpace = nodeLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assert.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine.children[1]
        Assert.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assert.assertEquals("firstKey = First value", nodePropertyLine.text)
        Assert.assertEquals(1, nodePropertyLine.children.size)
        nodePropertyLineChoice = nodePropertyLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_DEFINITION, nodePropertyLineChoice.rule)
        Assert.assertEquals("firstKey = First value", nodePropertyLineChoice.text)
        Assert.assertEquals(3, nodePropertyLineChoice.children.size)
        var nodeName = nodePropertyLineChoice.children[0]
        Assert.assertEquals(PROPERTIES_RULE_NAME, nodeName.rule)
        Assert.assertEquals("firstKey", nodeName.text)
        var nodeEquals = nodePropertyLineChoice.children[1]
        Assert.assertEquals("${PROPERTIES_RULE_DEFINITION}@1", nodeEquals.rule)
        Assert.assertEquals(" = ", nodeEquals.text)
        var nodeValue = nodePropertyLineChoice.children[2]
        Assert.assertEquals(PROPERTIES_RULE_VALUE, nodeValue.rule)
        Assert.assertEquals("First value", nodeValue.text)
        nodeEndLine = nodeLine.children[2]
        Assert.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assert.assertEquals("\n", nodeEndLine.text)

        // Line 6
        nodeLine = grammarNode.children[5]
        Assert.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assert.assertEquals("secondKey = Second value\n", nodeLine.text)
        Assert.assertEquals(3, nodeLine.children.size)
        nodeWhiteSpace = nodeLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assert.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine.children[1]
        Assert.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assert.assertEquals("secondKey = Second value", nodePropertyLine.text)
        Assert.assertEquals(1, nodePropertyLine.children.size)
        nodePropertyLineChoice = nodePropertyLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_DEFINITION, nodePropertyLineChoice.rule)
        Assert.assertEquals("secondKey = Second value", nodePropertyLineChoice.text)
        Assert.assertEquals(3, nodePropertyLineChoice.children.size)
        nodeName = nodePropertyLineChoice.children[0]
        Assert.assertEquals(PROPERTIES_RULE_NAME, nodeName.rule)
        Assert.assertEquals("secondKey", nodeName.text)
        nodeEquals = nodePropertyLineChoice.children[1]
        Assert.assertEquals("${PROPERTIES_RULE_DEFINITION}@1", nodeEquals.rule)
        Assert.assertEquals(" = ", nodeEquals.text)
        nodeValue = nodePropertyLineChoice.children[2]
        Assert.assertEquals(PROPERTIES_RULE_VALUE, nodeValue.rule)
        Assert.assertEquals("Second value", nodeValue.text)
        nodeEndLine = nodeLine.children[2]
        Assert.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assert.assertEquals("\n", nodeEndLine.text)

        // Line 7
        nodeLine = grammarNode.children[6]
        Assert.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assert.assertEquals("\n", nodeLine.text)
        Assert.assertEquals(3, nodeLine.children.size)
        nodeWhiteSpace = nodeLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assert.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine.children[1]
        Assert.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assert.assertEquals("", nodePropertyLine.text)
        Assert.assertEquals(1, nodePropertyLine.children.size)
        nodePropertyLineChoice = nodePropertyLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodePropertyLineChoice.rule)
        Assert.assertEquals("", nodePropertyLineChoice.text)
        nodeEndLine = nodeLine.children[2]
        Assert.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assert.assertEquals("\n", nodeEndLine.text)

        // Line 8
        nodeLine = grammarNode.children[7]
        Assert.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assert.assertEquals("# A comment\n", nodeLine.text)
        Assert.assertEquals(3, nodeLine.children.size)
        nodeWhiteSpace = nodeLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assert.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine.children[1]
        Assert.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assert.assertEquals("# A comment", nodePropertyLine.text)
        Assert.assertEquals(1, nodePropertyLine.children.size)
        nodePropertyLineChoice = nodePropertyLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_COMMENT, nodePropertyLineChoice.rule)
        Assert.assertEquals("# A comment", nodePropertyLineChoice.text)
        nodeEndLine = nodeLine.children[2]
        Assert.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assert.assertEquals("\n", nodeEndLine.text)

        // Line 9
        nodeLine = grammarNode.children[8]
        Assert.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assert.assertEquals("\n", nodeLine.text)
        Assert.assertEquals(3, nodeLine.children.size)
        nodeWhiteSpace = nodeLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assert.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine.children[1]
        Assert.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assert.assertEquals("", nodePropertyLine.text)
        Assert.assertEquals(1, nodePropertyLine.children.size)
        nodePropertyLineChoice = nodePropertyLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodePropertyLineChoice.rule)
        Assert.assertEquals("", nodePropertyLineChoice.text)
        nodeEndLine = nodeLine.children[2]
        Assert.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assert.assertEquals("\n", nodeEndLine.text)

        // Line 10
        nodeLine = grammarNode.children[9]
        Assert.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assert.assertEquals("thirdKey = Third value\n", nodeLine.text)
        Assert.assertEquals(3, nodeLine.children.size)
        nodeWhiteSpace = nodeLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assert.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine.children[1]
        Assert.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assert.assertEquals("thirdKey = Third value", nodePropertyLine.text)
        Assert.assertEquals(1, nodePropertyLine.children.size)
        nodePropertyLineChoice = nodePropertyLine.children[0]
        Assert.assertEquals(PROPERTIES_RULE_DEFINITION, nodePropertyLineChoice.rule)
        Assert.assertEquals("thirdKey = Third value", nodePropertyLineChoice.text)
        Assert.assertEquals(3, nodePropertyLineChoice.children.size)
        nodeName = nodePropertyLineChoice.children[0]
        Assert.assertEquals(PROPERTIES_RULE_NAME, nodeName.rule)
        Assert.assertEquals("thirdKey", nodeName.text)
        nodeEquals = nodePropertyLineChoice.children[1]
        Assert.assertEquals("${PROPERTIES_RULE_DEFINITION}@1", nodeEquals.rule)
        Assert.assertEquals(" = ", nodeEquals.text)
        nodeValue = nodePropertyLineChoice.children[2]
        Assert.assertEquals(PROPERTIES_RULE_VALUE, nodeValue.rule)
        Assert.assertEquals("Third value", nodeValue.text)
        nodeEndLine = nodeLine.children[2]
        Assert.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assert.assertEquals("\n", nodeEndLine.text)
    }
}