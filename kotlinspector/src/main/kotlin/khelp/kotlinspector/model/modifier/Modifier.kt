package khelp.kotlinspector.model.modifier

import khelp.grammar.GrammarNode

abstract class Modifier(val modifierType: ModifierType)
{
    internal abstract fun parse(grammarNode: GrammarNode)
}