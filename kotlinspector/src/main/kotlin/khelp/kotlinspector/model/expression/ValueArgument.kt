package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode

class ValueArgument
{
    var annotation = ""
        private set
    var name = ""
        private set
    var asStar = false
        private set
    val expression = Expression()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.name = ""
        this.annotation = grammarNode[0].text
        val nameNode = grammarNode[2]

        if (nameNode.numberChildren > 0)
        {
            this.name = nameNode[0].text
        }

        this.asStar = grammarNode[4].text == "*"
        this.expression.parse(grammarNode[6])
    }
}