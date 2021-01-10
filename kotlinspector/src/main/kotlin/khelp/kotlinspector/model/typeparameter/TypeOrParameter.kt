package khelp.kotlinspector.model.typeparameter

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.declaration.Parameter
import khelp.kotlinspector.model.type.Type

class TypeOrParameter
{
    var parameter:Parameter? = null
    private set
    var type : Type? = null
    private set

    internal fun parse(grammarNode: GrammarNode) {
        this.parameter = null
        this.type = null

        when(grammarNode.rule)
        {
            KotlinGrammar.parameter ->
            {
                this.parameter = Parameter()
                this.parameter?.parse(grammarNode)
            }
            KotlinGrammar.type ->
            {
                this.type = Type()
                this.type?.parse(grammarNode)
            }
        }
    }
}