package khelp.kotlinspector.model.modifier

import khelp.grammar.GrammarNode

class VisibilityModifier : Modifier(ModifierType.VISIBILITY)
{
    var visibilityModifierType = VisibilityModifierType.NONE
        private set

    override fun parse(grammarNode: GrammarNode)
    {
        this.visibilityModifierType = VisibilityModifierType.parse(grammarNode.text)
    }
}
