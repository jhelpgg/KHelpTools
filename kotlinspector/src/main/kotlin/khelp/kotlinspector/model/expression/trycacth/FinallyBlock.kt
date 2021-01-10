package khelp.kotlinspector.model.expression.trycacth

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.statement.Block

class FinallyBlock
{
    val block = Block()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.block.parse(grammarNode[2])
    }
}