package khelp.kotlinspector.model.statement

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.Expression

class WhileStatement
{
    val expression = Expression()
    var controlStructureBody: ControlStructureBody? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.controlStructureBody = null
        var node = grammarNode[0]
        this.expression.parse(node[4])
        node = node[8]

        if (node.rule == KotlinGrammar.controlStructureBody)
        {
            this.controlStructureBody = ControlStructureBody()
            this.controlStructureBody?.parse(node)
        }
    }
}