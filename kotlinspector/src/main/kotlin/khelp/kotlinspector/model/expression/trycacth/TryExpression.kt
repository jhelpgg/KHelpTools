package khelp.kotlinspector.model.expression.trycacth

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.statement.Block

class TryExpression
{
    val block = Block()
    var finallyBlock: FinallyBlock? = null
        private set

    private val catchBlocks = ArrayList<CatchBlock>()

    fun catchBlocks(): Array<CatchBlock> =
        this.catchBlocks.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.catchBlocks.clear()
        this.finallyBlock = null
        this.block.parse(grammarNode[2])
        var node = grammarNode[4]

        if (node[0].rule == KotlinGrammar.finallyBlock)
        {
            this.finallyBlock = FinallyBlock()
            this.finallyBlock?.parse(node[0])
        }
        else
        {
            node[0].forEachDeep(KotlinGrammar.catchBlock) { catchBlockNode ->
                val catchBlock = CatchBlock()
                catchBlock.parse(catchBlockNode)
                this.catchBlocks.add(catchBlock)
            }

            if (node[0].numberChildren > 1)
            {
                node = node[0][2]

                if (node.numberChildren > 0)
                {
                    this.finallyBlock = FinallyBlock()
                    this.finallyBlock?.parse(node[0])
                }
            }
        }
    }
}