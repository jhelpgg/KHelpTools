package khelp.kotlinspector.model.statement

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class ControlStructureBody
{
    var block: Block? = null
        private set
    var statement: Statement? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.block = null
        this.statement = null
        val node = grammarNode[0]

        when (node.rule)
        {
            KotlinGrammar.block     ->
            {
                this.block = Block()
                this.block?.parse(node)
            }
            KotlinGrammar.statement ->
            {
                this.statement = Statement()
                this.statement?.parse(node)
            }
        }
    }
}