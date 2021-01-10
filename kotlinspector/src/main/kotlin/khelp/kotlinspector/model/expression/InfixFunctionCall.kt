package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode

class InfixFunctionCall
{
    val firstRangeExpression = RangeExpression()

    private val nameRangeExpressionCouples = ArrayList<Pair<String, RangeExpression>>()

    fun nameRangeExpressionCouples(): Array<Pair<String, RangeExpression>> =
        this.nameRangeExpressionCouples.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.firstRangeExpression.parse(grammarNode[0])
        this.nameRangeExpressionCouples.clear()

        for (nameRangeExpressionCoupleNode in grammarNode[2])
        {
            val name = nameRangeExpressionCoupleNode[0].text
            val rangeExpression = RangeExpression()
            rangeExpression.parse(nameRangeExpressionCoupleNode[2])
            this.nameRangeExpressionCouples.add(Pair(name, rangeExpression))
        }
    }
}
