package khelp.kotlinspector.model.expression.literal.string

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class LineStringContent
{
    var lineStrText: LineStrText? = null
        private set
    var lineStrEscapedChar: LineStrEscapedChar? = null
        private set
    var lineStrRef: LineStrRef? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.lineStrText = null
        this.lineStrEscapedChar = null
        this.lineStrRef = null
        val node = grammarNode[0]

        when (node.rule)
        {
            KotlinGrammar.LineStrText        ->
            {
                this.lineStrText = LineStrText()
                this.lineStrText?.parse(node)
            }
            KotlinGrammar.LineStrEscapedChar ->
            {
                this.lineStrEscapedChar = LineStrEscapedChar()
                this.lineStrEscapedChar?.parse(node)
            }
            KotlinGrammar.LineStrRef         ->
            {
                this.lineStrRef = LineStrRef()
                this.lineStrRef?.parse(node)
            }
        }
    }
}