package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator

class NavigationSuffix
{
    var memberAccessOperator = MemberAccessOperator.NONE
        private set
    var name = ""
        private set
    var parenthesizedExpression: ParenthesizedExpression? = null
        private set
    var isClass = false
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.name = ""
        this.parenthesizedExpression = null
        this.isClass = false
        this.memberAccessOperator = MemberAccessOperator.parse(grammarNode[0].text)
        val node = grammarNode[2]

        when (node.rule)
        {
            KotlinGrammar.simpleIdentifier        -> this.name = node.text
            KotlinGrammar.parenthesizedExpression ->
            {
                this.parenthesizedExpression = ParenthesizedExpression()
                this.parenthesizedExpression?.parse(node)
            }
            else                                  -> this.isClass = true
        }
    }
}
