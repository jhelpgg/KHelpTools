package khelp.kotlinspector.model.statement

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.declaration.Declaration
import khelp.kotlinspector.model.expression.Expression
import khelp.kotlinspector.parseDeclarationInformation

class Statement
{
    var label = ""
        private set
    var annotation = ""
        private set
    var declaration: Declaration? = null
        private set
    var assignment: Assignment? = null
        private set
    var loopStatement: LoopStatement? = null
        private set
    var expression: Expression? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.label = ""
        this.annotation = ""
        this.declaration = null
        this.assignment = null
        this.loopStatement = null
        this.expression = null
        var node = grammarNode[0]

        if (node.numberChildren > 0)
        {
            node = node[0]

            when (node.rule)
            {
                KotlinGrammar.label      -> this.label = node.text
                KotlinGrammar.annotation -> this.annotation = node.text
            }
        }

        node = grammarNode[2][0]

        when (node.rule)
        {
            KotlinGrammar.declaration   -> this.declaration = parseDeclarationInformation(node)
            KotlinGrammar.assignment    ->
            {
                this.assignment = Assignment()
                this.assignment?.parse(node)
            }
            KotlinGrammar.loopStatement ->
            {
                this.loopStatement = LoopStatement()
                this.loopStatement?.parse(node)
            }
            KotlinGrammar.expression, KotlinGrammar.disjunction    ->
            {
                this.expression = Expression()
                this.expression?.parse(node)
            }
        }
    }
}