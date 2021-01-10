package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.expression.operator.MultiplicativeOperator

class MultiplicativeExpression
{
    val firstAsExpression = AsExpression()

    private val multiplicativeOperatorAsExpressionCouples = ArrayList<Pair<MultiplicativeOperator, AsExpression>>()

    fun multiplicativeOperatorAsExpressionCouples() : Array<Pair<MultiplicativeOperator, AsExpression>> =
        this.multiplicativeOperatorAsExpressionCouples.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.firstAsExpression.parse(grammarNode[0])
        this.multiplicativeOperatorAsExpressionCouples.clear()

        for(multiplicativeOperatorAsExpressionCoupleNode in grammarNode[2]) {
            val multiplicativeOperator = MultiplicativeOperator.parse(multiplicativeOperatorAsExpressionCoupleNode[0].text)
            val asExpression = AsExpression()
            asExpression.parse(multiplicativeOperatorAsExpressionCoupleNode[2])
            this.multiplicativeOperatorAsExpressionCouples.add(Pair(multiplicativeOperator, asExpression))
        }
    }
}
