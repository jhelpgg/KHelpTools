package khelp.kotlinspector.model.statement

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.expression.Expression

class DoWhileStatement
{
    val expression = Expression()
    var controlStructureBody: ControlStructureBody? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.controlStructureBody = null
        val node = grammarNode[2]

        if (node.numberChildren>0)
        {
            this.controlStructureBody = ControlStructureBody()
            this.controlStructureBody?.parse(node[0])
        }

        this.expression.parse(grammarNode[8])
    }
}