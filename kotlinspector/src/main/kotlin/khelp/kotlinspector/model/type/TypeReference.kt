package khelp.kotlinspector.model.type

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.delegation.UserType

class TypeReference
{
    var userType: UserType? = null
        private set
    var isDynamic = false
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.userType = null
        this.isDynamic = false
        val node = grammarNode[0]

        if (node.rule == KotlinGrammar.userType)
        {
            this.userType = UserType()
            this.userType?.parse(node)
        }
        else
        {
            isDynamic = true
        }
    }
}