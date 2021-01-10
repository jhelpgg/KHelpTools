package khelp.kotlinspector.model.declaration

import khelp.grammar.GrammarNode

abstract class Declaration(val declarationType: DeclarationType)
{
    internal abstract fun parse(grammarNode: GrammarNode)
}