package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode

class ParenthesizedExpression
{
    val expression = Expression()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.expression.parse(grammarNode[2])
    }
}