package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator

class NavigationSuffix
{
    var memberAccessOperator = MemberAccessOperator.NONE
        private set
    var expression: Expression? = null
        private set
    var isClass = false
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.expression = null
        this.isClass = false
        this.memberAccessOperator = MemberAccessOperator.parse(grammarNode[0].text)
        val node = grammarNode[2][0]

        when (node.rule)
        {
            KotlinGrammar.expression, KotlinGrammar.disjunction ->
            {
                this.expression = Expression()
                this.expression?.parse(node)
            }
            else                                                -> this.isClass = true
        }
    }
}
