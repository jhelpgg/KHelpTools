package khelp.kotlinspector.model.declaration

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillDelegationSpecifiers
import khelp.kotlinspector.fillModifiersAndReturnAnnotation
import khelp.kotlinspector.model.declaration.classinfo.ClassBody
import khelp.kotlinspector.model.delegation.AnnotatedDelegationSpecifier
import khelp.kotlinspector.model.modifier.Modifier

class ObjectDeclaration : Declaration(DeclarationType.OBJECT)
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

    override fun parse(grammarNode: GrammarNode)
    {
        this.classBody = null
        this.annotation = fillModifiersAndReturnAnnotation(grammarNode[0], this.modifiers)
        this.name = grammarNode[4].text
        val delegationSpecifiersNode = grammarNode[6]
        this.delegationSpecifiers.clear()

        if (delegationSpecifiersNode.numberChildren > 0)
        {
            fillDelegationSpecifiers(delegationSpecifiersNode[2], this.delegationSpecifiers)
        }

        val classBodyNode = grammarNode[8]

        if (classBodyNode.numberChildren > 0)
        {
            this.classBody = ClassBody()
            this.classBody?.parse(classBodyNode[0])
        }
    }
}