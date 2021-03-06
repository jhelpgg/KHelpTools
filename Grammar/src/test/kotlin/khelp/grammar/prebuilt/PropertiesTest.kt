package khelp.grammar.prebuilt

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PropertiesTest
{
    @Test
    fun propertiesTest()
    {
        val grammarNode = Properties.parse(PropertiesTest::class.java.getResourceAsStream("PropertiesTest.properties"))
        Assertions.assertNotNull(grammarNode)
        Assertions.assertEquals(PROPERTIES_RULE_PROPERTIES, grammarNode!!.rule)
        Assertions.assertEquals("""
            # ***************************************
            # *** Test of properties file parsing ***
            # ***************************************

            firstKey = First value
            secondKey = Second value

            # A comment

            thirdKey = Third value

        """.trimIndent(), grammarNode.text)
        Assertions.assertEquals(10, grammarNode.numberChildren)

        // Line 1
        var nodeLine = grammarNode[0]
        Assertions.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assertions.assertEquals("# ***************************************\n", nodeLine.text)
        Assertions.assertEquals(3, nodeLine.numberChildren)
        var nodeWhiteSpace = nodeLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assertions.assertEquals("", nodeWhiteSpace.text)
        var nodePropertyLine = nodeLine[1]
        Assertions.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assertions.assertEquals("# ***************************************", nodePropertyLine.text)
        Assertions.assertEquals(1, nodePropertyLine.numberChildren)
        var nodePropertyLineChoice = nodePropertyLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_COMMENT, nodePropertyLineChoice.rule)
        Assertions.assertEquals("# ***************************************", nodePropertyLineChoice.text)
        var nodeEndLine = nodeLine[2]
        Assertions.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assertions.assertEquals("\n", nodeEndLine.text)

        // Line 2
        nodeLine = grammarNode[1]
        Assertions.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assertions.assertEquals("# *** Test of properties file parsing ***\n", nodeLine.text)
        Assertions.assertEquals(3, nodeLine.numberChildren)
        nodeWhiteSpace = nodeLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assertions.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine[1]
        Assertions.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assertions.assertEquals("# *** Test of properties file parsing ***", nodePropertyLine.text)
        Assertions.assertEquals(1, nodePropertyLine.numberChildren)
        nodePropertyLineChoice = nodePropertyLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_COMMENT, nodePropertyLineChoice.rule)
        Assertions.assertEquals("# *** Test of properties file parsing ***", nodePropertyLineChoice.text)
        nodeEndLine = nodeLine[2]
        Assertions.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assertions.assertEquals("\n", nodeEndLine.text)

        // Line 3
        nodeLine = grammarNode[2]
        Assertions.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assertions.assertEquals("# ***************************************\n", nodeLine.text)
        Assertions.assertEquals(3, nodeLine.numberChildren)
        nodeWhiteSpace = nodeLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assertions.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine[1]
        Assertions.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assertions.assertEquals("# ***************************************", nodePropertyLine.text)
        Assertions.assertEquals(1, nodePropertyLine.numberChildren)
        nodePropertyLineChoice = nodePropertyLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_COMMENT, nodePropertyLineChoice.rule)
        Assertions.assertEquals("# ***************************************", nodePropertyLineChoice.text)
        nodeEndLine = nodeLine[2]
        Assertions.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assertions.assertEquals("\n", nodeEndLine.text)

        // Line 4
        nodeLine = grammarNode[3]
        Assertions.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assertions.assertEquals("\n", nodeLine.text)
        Assertions.assertEquals(3, nodeLine.numberChildren)
        nodeWhiteSpace = nodeLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assertions.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine[1]
        Assertions.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assertions.assertEquals("", nodePropertyLine.text)
        Assertions.assertEquals(1, nodePropertyLine.numberChildren)
        nodePropertyLineChoice = nodePropertyLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodePropertyLineChoice.rule)
        Assertions.assertEquals("", nodePropertyLineChoice.text)
        nodeEndLine = nodeLine[2]
        Assertions.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assertions.assertEquals("\n", nodeEndLine.text)

        // Line 5
        nodeLine = grammarNode[4]
        Assertions.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assertions.assertEquals("firstKey = First value\n", nodeLine.text)
        Assertions.assertEquals(3, nodeLine.numberChildren)
        nodeWhiteSpace = nodeLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assertions.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine[1]
        Assertions.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assertions.assertEquals("firstKey = First value", nodePropertyLine.text)
        Assertions.assertEquals(1, nodePropertyLine.numberChildren)
        nodePropertyLineChoice = nodePropertyLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_DEFINITION, nodePropertyLineChoice.rule)
        Assertions.assertEquals("firstKey = First value", nodePropertyLineChoice.text)
        Assertions.assertEquals(3, nodePropertyLineChoice.numberChildren)
        var nodeName = nodePropertyLineChoice[0]
        Assertions.assertEquals(PROPERTIES_RULE_NAME, nodeName.rule)
        Assertions.assertEquals("firstKey", nodeName.text)
        var nodeEquals = nodePropertyLineChoice[1]
        Assertions.assertEquals("${PROPERTIES_RULE_DEFINITION}@1", nodeEquals.rule)
        Assertions.assertEquals(" = ", nodeEquals.text)
        var nodeValue = nodePropertyLineChoice[2]
        Assertions.assertEquals(PROPERTIES_RULE_VALUE, nodeValue.rule)
        Assertions.assertEquals("First value", nodeValue.text)
        nodeEndLine = nodeLine[2]
        Assertions.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assertions.assertEquals("\n", nodeEndLine.text)

        // Line 6
        nodeLine = grammarNode[5]
        Assertions.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assertions.assertEquals("secondKey = Second value\n", nodeLine.text)
        Assertions.assertEquals(3, nodeLine.numberChildren)
        nodeWhiteSpace = nodeLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assertions.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine[1]
        Assertions.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assertions.assertEquals("secondKey = Second value", nodePropertyLine.text)
        Assertions.assertEquals(1, nodePropertyLine.numberChildren)
        nodePropertyLineChoice = nodePropertyLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_DEFINITION, nodePropertyLineChoice.rule)
        Assertions.assertEquals("secondKey = Second value", nodePropertyLineChoice.text)
        Assertions.assertEquals(3, nodePropertyLineChoice.numberChildren)
        nodeName = nodePropertyLineChoice[0]
        Assertions.assertEquals(PROPERTIES_RULE_NAME, nodeName.rule)
        Assertions.assertEquals("secondKey", nodeName.text)
        nodeEquals = nodePropertyLineChoice[1]
        Assertions.assertEquals("${PROPERTIES_RULE_DEFINITION}@1", nodeEquals.rule)
        Assertions.assertEquals(" = ", nodeEquals.text)
        nodeValue = nodePropertyLineChoice[2]
        Assertions.assertEquals(PROPERTIES_RULE_VALUE, nodeValue.rule)
        Assertions.assertEquals("Second value", nodeValue.text)
        nodeEndLine = nodeLine[2]
        Assertions.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assertions.assertEquals("\n", nodeEndLine.text)

        // Line 7
        nodeLine = grammarNode[6]
        Assertions.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assertions.assertEquals("\n", nodeLine.text)
        Assertions.assertEquals(3, nodeLine.numberChildren)
        nodeWhiteSpace = nodeLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assertions.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine[1]
        Assertions.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assertions.assertEquals("", nodePropertyLine.text)
        Assertions.assertEquals(1, nodePropertyLine.numberChildren)
        nodePropertyLineChoice = nodePropertyLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodePropertyLineChoice.rule)
        Assertions.assertEquals("", nodePropertyLineChoice.text)
        nodeEndLine = nodeLine[2]
        Assertions.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assertions.assertEquals("\n", nodeEndLine.text)

        // Line 8
        nodeLine = grammarNode[7]
        Assertions.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assertions.assertEquals("# A comment\n", nodeLine.text)
        Assertions.assertEquals(3, nodeLine.numberChildren)
        nodeWhiteSpace = nodeLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assertions.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine[1]
        Assertions.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assertions.assertEquals("# A comment", nodePropertyLine.text)
        Assertions.assertEquals(1, nodePropertyLine.numberChildren)
        nodePropertyLineChoice = nodePropertyLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_COMMENT, nodePropertyLineChoice.rule)
        Assertions.assertEquals("# A comment", nodePropertyLineChoice.text)
        nodeEndLine = nodeLine[2]
        Assertions.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assertions.assertEquals("\n", nodeEndLine.text)

        // Line 9
        nodeLine = grammarNode[8]
        Assertions.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assertions.assertEquals("\n", nodeLine.text)
        Assertions.assertEquals(3, nodeLine.numberChildren)
        nodeWhiteSpace = nodeLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assertions.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine[1]
        Assertions.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assertions.assertEquals("", nodePropertyLine.text)
        Assertions.assertEquals(1, nodePropertyLine.numberChildren)
        nodePropertyLineChoice = nodePropertyLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodePropertyLineChoice.rule)
        Assertions.assertEquals("", nodePropertyLineChoice.text)
        nodeEndLine = nodeLine[2]
        Assertions.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assertions.assertEquals("\n", nodeEndLine.text)

        // Line 10
        nodeLine = grammarNode[9]
        Assertions.assertEquals(PROPERTIES_RULE_LINE, nodeLine.rule)
        Assertions.assertEquals("thirdKey = Third value\n", nodeLine.text)
        Assertions.assertEquals(3, nodeLine.numberChildren)
        nodeWhiteSpace = nodeLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_WHITE_SPACE, nodeWhiteSpace.rule)
        Assertions.assertEquals("", nodeWhiteSpace.text)
        nodePropertyLine = nodeLine[1]
        Assertions.assertEquals(PROPERTIES_RULE_PROPERTY_LINE, nodePropertyLine.rule)
        Assertions.assertEquals("thirdKey = Third value", nodePropertyLine.text)
        Assertions.assertEquals(1, nodePropertyLine.numberChildren)
        nodePropertyLineChoice = nodePropertyLine[0]
        Assertions.assertEquals(PROPERTIES_RULE_DEFINITION, nodePropertyLineChoice.rule)
        Assertions.assertEquals("thirdKey = Third value", nodePropertyLineChoice.text)
        Assertions.assertEquals(3, nodePropertyLineChoice.numberChildren)
        nodeName = nodePropertyLineChoice[0]
        Assertions.assertEquals(PROPERTIES_RULE_NAME, nodeName.rule)
        Assertions.assertEquals("thirdKey", nodeName.text)
        nodeEquals = nodePropertyLineChoice[1]
        Assertions.assertEquals("${PROPERTIES_RULE_DEFINITION}@1", nodeEquals.rule)
        Assertions.assertEquals(" = ", nodeEquals.text)
        nodeValue = nodePropertyLineChoice[2]
        Assertions.assertEquals(PROPERTIES_RULE_VALUE, nodeValue.rule)
        Assertions.assertEquals("Third value", nodeValue.text)
        nodeEndLine = nodeLine[2]
        Assertions.assertEquals(PROPERTIES_RULE_END_LINE, nodeEndLine.rule)
        Assertions.assertEquals("\n", nodeEndLine.text)
    }
}