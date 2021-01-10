package khelp.kotlinspector.model.modifier

import khelp.grammar.GrammarNode

class ParameterModifier : Modifier(ModifierType.PARAMETER)
{
    var parameterModifierType = ParameterModifierType.NONE
        private set

    override fun parse(grammarNode: GrammarNode)
    {
        this.parameterModifierType = ParameterModifierType.parse(grammarNode.text)
    }
}
