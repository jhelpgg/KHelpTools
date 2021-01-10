package khelp.kotlinspector.model.declaration.classinfo

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.statement.Block

class AnonymousInitializer
{
    val block = Block()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.block.parse(grammarNode[2])
    }
}