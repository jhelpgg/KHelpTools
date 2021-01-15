package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class IndexingSuffix
{
    private val expressions = ArrayList<Expression>()

    fun expressions(): Array<Expression> =
        this.expressions.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.expressions.clear()
        grammarNode.forEachDeep(false,
                                { node -> node.rule == KotlinGrammar.expression || node.rule == KotlinGrammar.disjunction }) { expressionNode ->
            val expression = Expression()
            expression.parse(expressionNode)
            this.expressions.add(expression)
        }
    }
}
