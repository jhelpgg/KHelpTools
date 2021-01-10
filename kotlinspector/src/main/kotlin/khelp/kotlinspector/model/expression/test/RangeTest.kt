package khelp.kotlinspector.model.expression.test

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.expression.Expression
import khelp.kotlinspector.model.expression.operator.InOperator

class RangeTest
{
    var inOperator = InOperator.NONE
        private set
    val expression = Expression()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.inOperator = InOperator.parse(grammarNode[0].text)
        this.expression.parse(grammarNode[2])
    }
}