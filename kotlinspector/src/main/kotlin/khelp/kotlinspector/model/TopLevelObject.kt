package khelp.kotlinspector.model

import khelp.grammar.GrammarNode
import khelp.kotlinspector.model.declaration.Declaration
import khelp.kotlinspector.parseDeclarationInformation

class TopLevelObject
{
    var comments = ""
        private set
    lateinit var declaration: Declaration
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        val declarationNode = grammarNode[0]
        this.comments = declarationNode[0].text
        this.declaration = parseDeclarationInformation(declarationNode[2][0])
    }
}