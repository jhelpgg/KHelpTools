package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.expression.operator.AsOperator
import khelp.kotlinspector.model.type.Type

class AsExpression
{
    val prefixUnaryExpression = PrefixUnaryExpression()

    private val asOperatorTypeCouples = ArrayList<Pair<AsOperator, Type>>()

    fun asOperatorTypeCouples(): Array<Pair<AsOperator, Type>> =
        this.asOperatorTypeCouples.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.prefixUnaryExpression.parse(grammarNode[0])
        this.asOperatorTypeCouples.clear()

        for (asOperatorTypeCoupleNode in grammarNode[2])
        {
            val asOperator = AsOperator.parse(asOperatorTypeCoupleNode[0].text)
            val type = Type()
            type.parse(asOperatorTypeCoupleNode[2])
            this.asOperatorTypeCouples.add(Pair(asOperator, type))
        }
    }
}
