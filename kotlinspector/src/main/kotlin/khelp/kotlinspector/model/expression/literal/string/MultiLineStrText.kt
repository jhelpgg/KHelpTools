package khelp.kotlinspector.model.expression.literal.string

import khelp.grammar.GrammarNode

class MultiLineStrText
{
    var text = ""
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.text = grammarNode.text
    }
}