package khelp.kotlinspector.model.expression.literal.string

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class LineStringLiteral
{
    private val lineStringContentOrExpressions = ArrayList<LineStringContentOrExpression>()

    fun lineStringContentOrExpressions(): Array<LineStringContentOrExpression> =
        this.lineStringContentOrExpressions.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.lineStringContentOrExpressions.clear()

        for (lineStringContentOrExpressionNode in grammarNode[2])
        {
            val node = lineStringContentOrExpressionNode[0]
            val lineStringContentOrExpression =
                if (node.rule == KotlinGrammar.lineStringContent)
                {
                    val lineStringContent = LineStringContent()
                    lineStringContent.parse(node)
                    LineStringContentOrExpression(lineStringContent)
                }
                else
                {
                    val lineStringExpression = LineStringExpression()
                    lineStringExpression.parse(node)
                    LineStringContentOrExpression(lineStringExpression)
                }
            this.lineStringContentOrExpressions.add(lineStringContentOrExpression)
        }
    }
}