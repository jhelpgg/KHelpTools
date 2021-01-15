package khelp.kotlinspector.model.modifier

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class ParameterModifiers
{
    var annotation = ""
    private set

    private val parameterModifiers = ArrayList<ParameterModifier>()

    fun parameterModifiers() : Array<ParameterModifier> =
        this.parameterModifiers.toTypedArray()

     fun parse(grammarNode: GrammarNode)
    {
        this.annotation = ""
        this.parameterModifiers.clear()
        val contentNode = grammarNode[0]

        if(contentNode.rule == KotlinGrammar.annotation)
        {
            this.annotation = contentNode.text
        }
        else
        {
            for(parameterModifierNode in contentNode)
            {
                val parameterModifier = ParameterModifier()
                parameterModifier.parse(parameterModifierNode)
                this.parameterModifiers.add(parameterModifier)
            }
        }
    }
}