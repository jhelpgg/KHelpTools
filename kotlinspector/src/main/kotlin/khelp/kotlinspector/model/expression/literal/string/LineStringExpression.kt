package khelp.kotlinspector.model.expression.literal.string

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.expression.Expression

class LineStringExpression
{
    val expression = Expression()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.expression.parse(grammarNode[2])
    }
}