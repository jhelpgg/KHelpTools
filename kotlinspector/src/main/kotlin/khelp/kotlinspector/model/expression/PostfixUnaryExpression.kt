package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class PostfixUnaryExpression
{
    val primaryExpression = PrimaryExpression()

    private val postfixUnarySuffixs = ArrayList<PostFixUnarySuffix>()

    fun postfixUnarySuffixs(): Array<PostFixUnarySuffix> =
        this.postfixUnarySuffixs.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        val node = grammarNode[0]

        if (node.rule == KotlinGrammar.primaryExpression)
        {
            this.primaryExpression.parse(node)
        }
        else
        {
            this.primaryExpression.parse(node[0])
        }

        this.postfixUnarySuffixs.clear()

        if (node.numberChildren > 2)
        {
            for (postfixUnarySuffixNode in node[2])
            {
                val postfixUnarySuffix = PostFixUnarySuffix()
                postfixUnarySuffix.parse(postfixUnarySuffixNode)
                this.postfixUnarySuffixs.add(postfixUnarySuffix)
            }
        }
    }
}
