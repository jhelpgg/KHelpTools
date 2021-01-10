package khelp.kotlinspector.model.expression.literal

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillDelegationSpecifiers
import khelp.kotlinspector.model.declaration.classinfo.ClassBody
import khelp.kotlinspector.model.delegation.AnnotatedDelegationSpecifier

class ObjectLiteral
{
    val classBody = ClassBody()

    private val delegationSpecifiers = ArrayList<AnnotatedDelegationSpecifier>()

    fun delegationSpecifiers(): Array<AnnotatedDelegationSpecifier> =
        this.delegationSpecifiers.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.delegationSpecifiers.clear()
        val node = grammarNode[0]
        val classBodyIndex =
            if (node.numberChildren > 3)
            {
                fillDelegationSpecifiers(node[4], this.delegationSpecifiers)
                6
            }
            else
            {
                2
            }
        this.classBody.parse(node[classBodyIndex])
    }
}