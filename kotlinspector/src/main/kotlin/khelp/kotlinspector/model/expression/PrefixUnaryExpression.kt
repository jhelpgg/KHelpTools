package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode

class PrefixUnaryExpression
{
    val postfixUnaryExpression = PostfixUnaryExpression()
    private val unaryPrefixs = ArrayList<UnaryPrefix>()

    fun unaryPrefixs(): Array<UnaryPrefix> =
        this.unaryPrefixs.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.unaryPrefixs.clear()

        for (unaryPrefixNode in grammarNode[0])
        {
            val unaryPrefix = UnaryPrefix()
            unaryPrefix.parse(unaryPrefixNode)
            this.unaryPrefixs.add(unaryPrefix)
        }

        this.postfixUnaryExpression.parse(grammarNode[2])
    }
}
