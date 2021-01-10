package khelp.kotlinspector.model.declaration

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.type.Type

class VariableDeclaration
{
    var annotation = ""
        private set
    var name = ""
        private set
    var type: Type? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.type = null
        this.annotation = grammarNode[0].text
        this.name = grammarNode[2].text
        val typeNode = grammarNode[4]

        if (typeNode.numberChildren > 0)
        {
            this.type = Type()
            this.type?.parse(typeNode[2])
        }
    }
}