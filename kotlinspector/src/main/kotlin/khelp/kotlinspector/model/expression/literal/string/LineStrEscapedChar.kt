package khelp.kotlinspector.model.expression.literal.string

import khelp.grammar.GrammarNode

class LineStrEscapedChar
{
    var escaped = ""
    private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.escaped = grammarNode.text
    }
}