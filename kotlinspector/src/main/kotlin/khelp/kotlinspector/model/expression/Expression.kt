package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode

class Expression
{
    lateinit var disjunction: Disjunction

    internal fun parse(grammarNode: GrammarNode)
    {
        this.disjunction = Disjunction()
        this.disjunction.parse(grammarNode)
    }
}
