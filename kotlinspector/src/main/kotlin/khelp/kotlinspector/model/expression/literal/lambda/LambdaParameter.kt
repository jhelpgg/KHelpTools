package khelp.kotlinspector.model.expression.literal.lambda

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillVariableDeclarations
import khelp.kotlinspector.model.declaration.VariableDeclaration
import khelp.kotlinspector.model.type.Type

class LambdaParameter
{
    var type: Type? = null
        private set
    private val variableDeclarations = ArrayList<VariableDeclaration>()

    fun variableDeclarations(): Array<VariableDeclaration> =
        this.variableDeclarations.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.type = null
        fillVariableDeclarations(grammarNode[0], this.variableDeclarations)
        val node = grammarNode[2]

        if (node.numberChildren > 0)
        {
            this.type = Type()
            this.type?.parse(node[0][2])
        }
    }
}