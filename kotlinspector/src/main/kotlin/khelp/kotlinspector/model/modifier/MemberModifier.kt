package khelp.kotlinspector.model.modifier

import khelp.grammar.GrammarNode

class MemberModifier : Modifier(ModifierType.MEMBER)
{
    var memberModifierType = MemberModifierType.NONE
        private set

    override fun parse(grammarNode: GrammarNode)
    {
        this.memberModifierType = MemberModifierType.parse(grammarNode.text)
    }
}
