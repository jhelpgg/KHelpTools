package khelp.kotlinspector.model.declaration

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.type.Type

class Parameter
{
    var name = ""
        private set
    val type = Type()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.name = grammarNode[0].text
        this.type.parse(grammarNode[4])
    }
}