package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.statement.ControlStructureBody

class IfExpression
{
    val expression = Expression()
    var controlStructureBody : ControlStructureBody?=null
    private set
    var elseControlStructureBody : ControlStructureBody?=null
    private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.controlStructureBody=null
        this.elseControlStructureBody = null
        val node = grammarNode[0]
        this.expression.parse(node[4])
        val controlStructureBodyNode = node[6]

        if(controlStructureBodyNode.numberChildren>0)
        {
            this.controlStructureBody = ControlStructureBody()
            this.controlStructureBody?.parse(controlStructureBodyNode)
        }

        if(node.numberChildren>9)
        {
            val elseControlStructureBodyNode = node[12]

            if(elseControlStructureBodyNode.numberChildren>0)
            {
                this.elseControlStructureBody = ControlStructureBody()
                this.elseControlStructureBody?.parse(elseControlStructureBodyNode[0])
            }
        }
    }
}