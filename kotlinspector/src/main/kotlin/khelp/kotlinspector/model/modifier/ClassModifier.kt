package khelp.kotlinspector.model.modifier

import khelp.grammar.GrammarNode

class ClassModifier : Modifier(ModifierType.CLASS)
{
    var classModifierType = ClassModifierType.NONE
        private set

    override fun parse(grammarNode: GrammarNode)
    {
        this.classModifierType = ClassModifierType.parse(grammarNode.text)
    }
}
