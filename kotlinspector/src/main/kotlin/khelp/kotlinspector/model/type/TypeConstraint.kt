package khelp.kotlinspector.model.type

import khelp.grammar.GrammarNode

class TypeConstraint
{
    var annotation = ""
        private set
    var name = ""
        private set
    val type = Type()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.annotation = grammarNode[0].text
        this.name = grammarNode[2].text
        this.type.parse(grammarNode[6])
    }
}