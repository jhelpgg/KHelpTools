package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode

class ParenthesizedDirectlyAssignableExpression
{
    val directlyAssignableExpression = DirectlyAssignableExpression()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.directlyAssignableExpression.parse(grammarNode[2])
    }
}
