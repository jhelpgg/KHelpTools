package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode

class ParenthesizedAssignableExpression
{
    val assignableExpression = AssignableExpression()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.assignableExpression.parse(grammarNode[2])
    }
}