package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class DirectlyAssignableExpression
{
    var postfixUnaryExpression: PostfixUnaryExpression? = null
        private set
    var parenthesizedDirectlyAssignableExpression: ParenthesizedDirectlyAssignableExpression? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.postfixUnaryExpression = null
        this.parenthesizedDirectlyAssignableExpression = null
        val node = grammarNode[0]

        if (node.rule ==    KotlinGrammar.    postfixUnaryExpression)
        {
            this.postfixUnaryExpression = PostfixUnaryExpression()
            this.postfixUnaryExpression?.parse(node)
        }
        else
        {
            this.parenthesizedDirectlyAssignableExpression = ParenthesizedDirectlyAssignableExpression()
            this.parenthesizedDirectlyAssignableExpression?.parse(node)
        }
    }
}