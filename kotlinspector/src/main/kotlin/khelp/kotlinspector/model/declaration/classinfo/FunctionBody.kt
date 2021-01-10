package khelp.kotlinspector.model.declaration.classinfo

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.Expression
import khelp.kotlinspector.model.statement.Block

class FunctionBody
{
    var block: Block? = null
        private set
    var expression: Expression? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.block = null
        this.expression = null
        val node = grammarNode[0]

        if (node.rule == KotlinGrammar.block)
        {
            this.block = Block()
            this.block?.parse(node)
        }
        else
        {
            this.expression = Expression()
            this.expression?.parse(node[2])
        }
    }
}