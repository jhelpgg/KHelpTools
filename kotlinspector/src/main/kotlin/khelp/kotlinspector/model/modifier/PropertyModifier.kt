package khelp.kotlinspector.model.modifier

import khelp.grammar.GrammarNode

/**
 * const
 */
class PropertyModifier : Modifier(ModifierType.PROPERTY)
{
    override fun parse(grammarNode: GrammarNode) = Unit
}
