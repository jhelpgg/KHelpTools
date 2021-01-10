package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class AssignableExpression
{
    var prefixUnaryExpression: PrefixUnaryExpression? = null
        private set
    var parenthesizedAssignableExpression: ParenthesizedAssignableExpression? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.prefixUnaryExpression = null
        this.parenthesizedAssignableExpression = null
        val node = grammarNode[0]

        when (node.rule)
        {
            KotlinGrammar.prefixUnaryExpression             ->
            {
                this.prefixUnaryExpression = PrefixUnaryExpression()
                this.prefixUnaryExpression?.parse(node)
            }
            KotlinGrammar.parenthesizedAssignableExpression ->
            {
                this.parenthesizedAssignableExpression = ParenthesizedAssignableExpression()
                this.parenthesizedAssignableExpression?.parse(node)
            }
        }
    }
}