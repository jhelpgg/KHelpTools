package khelp.kotlinspector.model.expression.`when`

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.declaration.VariableDeclaration
import khelp.kotlinspector.model.expression.Expression

class WhenSubject
{
    var declarationAnnotation = ""
    private set
    var variableDeclaration : VariableDeclaration?=null
    private set
    val expression = Expression()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.declarationAnnotation = ""
        this.variableDeclaration = null
        val node = grammarNode[2]

        if(node.numberChildren>0)
        {
            this.declarationAnnotation = node[0].text
            this.variableDeclaration = VariableDeclaration()
            this.variableDeclaration?.parse(node[4])
        }

        this.expression.parse(grammarNode[4])
    }
}