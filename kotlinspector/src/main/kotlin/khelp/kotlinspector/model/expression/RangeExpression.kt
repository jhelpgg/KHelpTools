package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class RangeExpression
{
    private val additiveExpressions = ArrayList<AdditiveExpression>()

    fun additiveExpressions(): Array<AdditiveExpression> =
        this.additiveExpressions.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.additiveExpressions.clear()
        grammarNode.forEachDeep(KotlinGrammar.additiveExpression) { additiveExpressionNode ->
            val additiveExpression = AdditiveExpression()
            additiveExpression.parse(additiveExpressionNode)
            this.additiveExpressions.add(additiveExpression)
        }
    }
}
