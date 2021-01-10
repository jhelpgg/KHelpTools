package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class DirectlyAssignableExpression
{
    var postfixUnaryExpression: PostfixUnaryExpression? = null
        private set
    var assignableSuffix: AssignableSuffix? = null
        private set
    var name = ""
        private set
    var parenthesizedDirectlyAssignableExpression: ParenthesizedDirectlyAssignableExpression? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.postfixUnaryExpression = null
        this.assignableSuffix = null
        this.name = ""
        this.parenthesizedDirectlyAssignableExpression = null
        val node = grammarNode[0]

        when
        {
            node.numberChildren > 1                     ->
            {
                this.postfixUnaryExpression = PostfixUnaryExpression()
                this.postfixUnaryExpression?.parse(node[0])
                this.assignableSuffix = AssignableSuffix()
                this.assignableSuffix?.parse(node[2])
            }
            node.rule == KotlinGrammar.simpleIdentifier ->
            {
                this.name = node.text
            }
            else                                        ->
            {
                this.parenthesizedDirectlyAssignableExpression = ParenthesizedDirectlyAssignableExpression()
                this.parenthesizedDirectlyAssignableExpression?.parse(node)
            }
        }
    }
}