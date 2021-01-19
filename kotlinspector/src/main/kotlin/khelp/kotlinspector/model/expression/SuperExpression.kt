package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.type.Type

class SuperExpression
{
    var type: Type? = null
        private set
    var simpleIdentifier = ""
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.type = null
        this.simpleIdentifier = ""
        var node = grammarNode[2]

        if (node.numberChildren > 0)
        {
            this.type = Type()
            this.type?.parse(node[0][2])
        }

        node = grammarNode[4]

        if (node.numberChildren > 0)
        {
            this.simpleIdentifier = node[0][2].text
        }
    }
}