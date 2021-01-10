package khelp.kotlinspector.model.type

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.delegation.UserType

class ParenthesizedUserType
{
    var userType : UserType?=null
    private set
    var parenthesizedUserType : ParenthesizedUserType?=null
    private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.userType = null
        this.parenthesizedUserType = null
        val node = grammarNode[0][2]

        when(node.rule)
        {
            KotlinGrammar.userType->
            {
                this.userType = UserType()
                this.userType?.parse(node)
            }
            KotlinGrammar.parenthesizedUserType ->
            {
                this.parenthesizedUserType = ParenthesizedUserType()
                this.parenthesizedUserType?.parse(node)
            }
        }
    }
}