package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.expression.operator.AdditiveOperator

class AdditiveExpression
{
    val firstMultiplicativeExpression = MultiplicativeExpression()

    private val additiveOperatorMultiplicativeExpressionCouples = ArrayList<Pair<AdditiveOperator, MultiplicativeExpression>>()

    fun additiveOperatorMultiplicativeExpressionCouples() : Array<Pair<AdditiveOperator, MultiplicativeExpression>> =
        this.additiveOperatorMultiplicativeExpressionCouples.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.firstMultiplicativeExpression.parse(grammarNode[0])
        this.additiveOperatorMultiplicativeExpressionCouples.clear()

        for(additiveOperatorMultiplicativeExpressionCoupleNode in grammarNode[2]) {
            val additiveOperator = AdditiveOperator.parse(additiveOperatorMultiplicativeExpressionCoupleNode[0].text)
            val multiplicativeExpression = MultiplicativeExpression()
            multiplicativeExpression.parse(additiveOperatorMultiplicativeExpressionCoupleNode[2])
            this.additiveOperatorMultiplicativeExpressionCouples.add(Pair(additiveOperator,multiplicativeExpression))
        }
    }
}
