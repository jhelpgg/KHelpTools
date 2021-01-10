package khelp.kotlinspector.model.expression.literal.string

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class MultiLineStringLiteral
{
    private val multilineStringContentOrExpressions = ArrayList<MultilineStringContentOrExpression>()

    fun multilineStringContentOrExpressions(): Array<MultilineStringContentOrExpression> =
        this.multilineStringContentOrExpressions.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.multilineStringContentOrExpressions.clear()

        for (multilineStringContentOrExpressionNode in grammarNode[2])
        {
            val node = multilineStringContentOrExpressionNode[0]
            val multilineStringContentOrExpression =
                when (node.rule)
                {
                    KotlinGrammar.multiLineStringContent    ->
                    {
                        val multilineStringContent = MultiLineStringContent()
                        multilineStringContent.parse(node)
                        MultilineStringContentOrExpression(multilineStringContent)
                    }
                    KotlinGrammar.multiLineStringExpression ->
                    {
                        val multilineStringExpression = MultiLineStringExpression()
                        multilineStringExpression.parse(node)
                        MultilineStringContentOrExpression(multilineStringExpression)
                    }
                    else                                    -> MultilineStringContentOrExpression()
                }
            this.multilineStringContentOrExpressions.add(multilineStringContentOrExpression)
        }
    }
}