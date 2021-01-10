package khelp.kotlinspector.model.modifier

import khelp.grammar.GrammarNode

class FunctionModifier : Modifier(ModifierType.FUNCTION)
{
    var functionModifierType = FunctionModifierType.NONE
        private set

    override fun parse(grammarNode: GrammarNode)
    {
        this.functionModifierType = FunctionModifierType.parse(grammarNode.text)
    }
}
