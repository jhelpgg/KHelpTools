package khelp.kotlinspector.model.declaration

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.modifier.ParameterModifiers
import khelp.kotlinspector.model.type.Type

class ParameterWithOptionalType
{
    var parameterModifiers : ParameterModifiers? = null
    private set
    var name = ""
    private set
    var type:Type?=null
    private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.parameterModifiers = null
        this.type = null
        val parameterModifiersNode = grammarNode[0]

        if(parameterModifiersNode.numberChildren>0)
        {
            this.parameterModifiers = ParameterModifiers()
            this.parameterModifiers?.parse(parameterModifiersNode[0])
        }

        this.name = grammarNode[2].text
        val typeNode = grammarNode[4]

        if(typeNode.numberChildren>0)
        {
            this.type = Type()
            this.type?.parse(typeNode[0][2])
        }
    }
}