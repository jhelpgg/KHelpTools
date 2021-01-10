package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.expression.operator.EqualityOperator

class Equality
{
    lateinit var firstComparison: Comparison
    private val operatorComparisonCouples = ArrayList<Pair<EqualityOperator, Comparison>>()

    fun operatorComparisonCouples(): Array<Pair<EqualityOperator, Comparison>> =
        this.operatorComparisonCouples.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.firstComparison = Comparison()
        this.firstComparison.parse(grammarNode[0])
        this.operatorComparisonCouples.clear()

        for (child in grammarNode[2])
        {
            val operator = EqualityOperator.parse(child[0].text)
            val comparison = Comparison()
            comparison.parse(child[2])
            this.operatorComparisonCouples.add(Pair(operator, comparison))
        }
    }
}
