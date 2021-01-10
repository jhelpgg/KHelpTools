package khelp.kotlinspector.model.modifier

import khelp.grammar.GrammarNode

class InheritanceModifier : Modifier(ModifierType.INHERITANCE)
{
    var inheritanceModifierType = InheritanceModifierType.NONE
        private set

    override fun parse(grammarNode: GrammarNode)
    {
        this.inheritanceModifierType = InheritanceModifierType.parse(grammarNode.text)
    }
}
