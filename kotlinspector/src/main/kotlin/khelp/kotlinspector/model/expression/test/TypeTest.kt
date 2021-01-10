package khelp.kotlinspector.model.expression.test

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.expression.operator.IsOperator
import khelp.kotlinspector.model.type.Type

class TypeTest
{
    var isOperator = IsOperator.NONE
        private set
    val type = Type()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.isOperator = IsOperator.parse(grammarNode[0].text)
        this.type.parse(grammarNode[2])
    }
}