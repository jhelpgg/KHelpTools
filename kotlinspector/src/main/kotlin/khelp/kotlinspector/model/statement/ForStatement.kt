package khelp.kotlinspector.model.statement

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillVariableDeclarations
import khelp.kotlinspector.model.declaration.VariableDeclaration
import khelp.kotlinspector.model.expression.Expression

class ForStatement
{
    var annotation = ""
        private set
    val expression = Expression()
    var controlStructureBody: ControlStructureBody? = null
        private set

    private val variableDeclarations = ArrayList<VariableDeclaration>()

    fun variableDeclarations(): Array<VariableDeclaration> =
        this.variableDeclarations.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.controlStructureBody = null
        this.annotation = grammarNode[4].text
        fillVariableDeclarations(grammarNode[6], this.variableDeclarations)
        this.expression.parse(grammarNode[10])
        val node = grammarNode[14]

        if (node.numberChildren > 0)
        {
            this.controlStructureBody = ControlStructureBody()
            this.controlStructureBody?.parse(node[0])
        }
    }
}