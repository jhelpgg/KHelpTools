package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.type.Type

class SuperExpression
{
    var type:Type?= null
    private set
    var simpleIdentifier= ""
    private set
     var atIdentifier = ""
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.type = null
        this.simpleIdentifier = ""
        this.atIdentifier = ""
        val parentNode = grammarNode[0]

        if(parentNode.rule == KotlinGrammar.SUPER_AT)
        {
            this.atIdentifier = parentNode[2].text
        }
        else
        {
            var node = parentNode[2]

            if (node.numberChildren > 0)
            {
                this.type = Type()
                this.type?.parse(node[2])
            }

            node = parentNode[4]

            if (node.numberChildren > 0)
            {
                this.simpleIdentifier = node[2].text
            }
        }
    }
}