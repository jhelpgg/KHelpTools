package khelp.kotlinspector.model.expression.literal.string

import khelp.grammar.GrammarNode

class LineStrRef
{
    var reference = ""
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.reference = grammarNode[0][2].text
    }
}