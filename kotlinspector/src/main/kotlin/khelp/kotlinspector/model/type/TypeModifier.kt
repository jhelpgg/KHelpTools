package khelp.kotlinspector.model.type

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class TypeModifier
{
    var annotation = ""
        private set
    var isSuspend = false
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.annotation = ""
        this.isSuspend = false
        val node = grammarNode[0]

        if (node.rule == KotlinGrammar.annotation)
        {
            this.annotation = node.text
        }
        else
        {
            this.isSuspend = true
        }
    }
}