package khelp.kotlinspector.model.statement

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillStatements

class Block
{
    private val statements = ArrayList<Statement>()

    fun statements(): Array<Statement> =
        this.statements.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        fillStatements(grammarNode[2], this.statements)
    }
}