package khelp.kotlinspector.model.expression.trycacth

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.statement.Block
import khelp.kotlinspector.model.type.Type

class CatchBlock
{
    var annotation = ""
    private set
    var simpleIdentifier = ""
    private set
    val type = Type()
    val block = Block()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.annotation = grammarNode[4].text
        this.simpleIdentifier = grammarNode[6].text
        this.type.parse(grammarNode[10])
        this.block.parse(grammarNode[16])
    }
}