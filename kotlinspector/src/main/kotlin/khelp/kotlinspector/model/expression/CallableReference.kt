package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.type.ReceiverType

class CallableReference
{
    var receiverType: ReceiverType? = null
        private set
    var simpleIdentifier = ""
        private set
    var isClasReference = false
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.receiverType = null
        this.simpleIdentifier = ""
        this.isClasReference = false
        var node = grammarNode[0]

        if (node.numberChildren > 0)
        {
            this.receiverType = ReceiverType()
            this.receiverType?.parse(node[0])
        }

        node = grammarNode[4][0]

        if (node.rule == KotlinGrammar.simpleIdentifier)
        {
            this.simpleIdentifier = node.text
        }
        else
        {
            this.isClasReference = true
        }
    }
}