package khelp.kotlinspector.model.type

import khelp.grammar.GrammarNode

class ParenthesizedType
{
    val type = Type()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.type.parse(grammarNode[2])
    }
}