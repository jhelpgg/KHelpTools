package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.expression.operator.ComparisonOperator

class Comparison
{
    lateinit var firstGenericCallLikeComparison: GenericCallLikeComparison
    private val operatorGenericCallLikeComparisonCouples = ArrayList<Pair<ComparisonOperator, GenericCallLikeComparison>>()

    fun operatorGenericCallLikeComparisonCouples(): Array<Pair<ComparisonOperator, GenericCallLikeComparison>> =
        this.operatorGenericCallLikeComparisonCouples.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.firstGenericCallLikeComparison = GenericCallLikeComparison()
        this.firstGenericCallLikeComparison.parse(grammarNode[0])
        this.operatorGenericCallLikeComparisonCouples.clear()

        for (child in grammarNode[2])
        {
            val operator = ComparisonOperator.parse(child[0].text)
            val comparison = GenericCallLikeComparison()
            comparison.parse(child[2])
            this.operatorGenericCallLikeComparisonCouples.add(Pair(operator, comparison))
        }
    }
}
