package khelp.kotlinspector.model.declaration.classinfo

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillDelegationSpecifiers
import khelp.kotlinspector.fillModifiersAndReturnAnnotation
import khelp.kotlinspector.model.delegation.AnnotatedDelegationSpecifier
import khelp.kotlinspector.model.modifier.Modifier

class CompanionObject
{
    var annotation = ""
        private set
    var name = ""
        private set
    var classBody: ClassBody? = null
        private set
    private val modifiers = ArrayList<Modifier>()
    private val delegationSpecifiers = ArrayList<AnnotatedDelegationSpecifier>()

    fun modifiers(): Array<Modifier> =
        this.modifiers.toTypedArray()

    fun delegationSpecifiers(): Array<AnnotatedDelegationSpecifier> =
        this.delegationSpecifiers.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.annotation = fillModifiersAndReturnAnnotation(grammarNode[0], this.modifiers)
        this.name = grammarNode[6].text
        fillDelegationSpecifiers(grammarNode[8], this.delegationSpecifiers)
        val classBodyNode = grammarNode[10]

        if (classBodyNode.numberChildren > 0)
        {
            this.classBody = ClassBody()
            this.classBody?.parse(classBodyNode)
        }
        else
        {
            this.classBody = null
        }
    }
}