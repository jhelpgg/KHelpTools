package khelp.kotlinspector.model.statement

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class LoopStatement
{
    var forStatement :ForStatement?=null
    private set
    var whileStatement : WhileStatement?=null
    private set
    var doWhileStatement : DoWhileStatement?=null
    private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.forStatement=null
        this.whileStatement=null
        this.doWhileStatement = null
        val node = grammarNode[0]

        when(node.rule)
        {
            KotlinGrammar.forStatement ->
            {
                this.forStatement = ForStatement()
                forStatement?.parse(node)
            }
            KotlinGrammar.whileStatement ->
            {
                this.whileStatement = WhileStatement()
                whileStatement?.parse(node)
            }
            KotlinGrammar.doWhileStatement ->
            {
                this.doWhileStatement = DoWhileStatement()
                doWhileStatement?.parse(node)
            }
        }
    }
}