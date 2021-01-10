package khelp.kotlinspector.model.type

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.expression.TypeArguments

class SimpleUserType
{
    var name = ""
    private set
    var typeArguments : TypeArguments? = null
    private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.typeArguments=null
        this.name = grammarNode[0].text
        val typeArgumentsNode = grammarNode[2]

        if(typeArgumentsNode.numberChildren>0)
        {
            this.typeArguments = TypeArguments()
            this.typeArguments?.parse(typeArgumentsNode[0])
        }
    }
}