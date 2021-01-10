package khelp.kotlinspector.model.modifier

import khelp.grammar.GrammarNode

class PlatformModifier : Modifier(ModifierType.PLATFORM)
{
    var platformModifierType = PlatformModifierType.NONE
        private set

    override fun parse(grammarNode: GrammarNode)
    {
        this.platformModifierType = PlatformModifierType.parse(grammarNode.text)
    }
}
