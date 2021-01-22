package khelp.kotlinspector.model.expression.literal.string

import khelp.grammar.GrammarNode

class MultiLineStrRef
{
     var reference = ""
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.reference = grammarNode[1].text
    }
}