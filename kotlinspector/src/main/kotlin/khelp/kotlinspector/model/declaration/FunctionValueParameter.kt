package khelp.kotlinspector.model.declaration

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.expression.Expression
import khelp.kotlinspector.model.modifier.ParameterModifiers

class FunctionValueParameter
{
    var parameterModifiers : ParameterModifiers? = null
    private set
    val parameter = Parameter()
    var expression : Expression? = null
    private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.parameterModifiers = null
        this.expression = null
        val parameterModifiersNode = grammarNode[0]

        if(parameterModifiersNode.numberChildren>0)
        {
            this.parameterModifiers = ParameterModifiers()
            this.parameterModifiers?.parse(parameterModifiersNode[0])
        }

        this.parameter.parse(grammarNode[2])

        val expressionNode = grammarNode[4]

        if(expressionNode.numberChildren>0)
        {
            this.expression = Expression()
            this.expression?.parse(expressionNode[2])
        }
    }
}