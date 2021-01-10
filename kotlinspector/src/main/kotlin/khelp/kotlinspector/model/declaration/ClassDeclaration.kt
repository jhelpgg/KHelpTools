package khelp.kotlinspector.model.declaration

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.fillDelegationSpecifiers
import khelp.kotlinspector.fillModifiersAndReturnAnnotation
import khelp.kotlinspector.fillTypeConstraints
import khelp.kotlinspector.fillTypeParameters
import khelp.kotlinspector.model.declaration.classinfo.Body
import khelp.kotlinspector.model.declaration.classinfo.ClassBody
import khelp.kotlinspector.model.declaration.classinfo.EnumBody
import khelp.kotlinspector.model.declaration.classinfo.PrimaryConstructor
import khelp.kotlinspector.model.delegation.AnnotatedDelegationSpecifier
import khelp.kotlinspector.model.modifier.Modifier
import khelp.kotlinspector.model.type.TypeConstraint
import khelp.kotlinspector.model.typeparameter.TypeParameter


class ClassDeclaration : Declaration(DeclarationType.CLASS)
{
    var annotation = ""
        private set
    lateinit var classType: ClassType
        private set
    var className = ""
        private set
    var primaryConstructor: PrimaryConstructor? = null
        private set
    var body: Body? = null
        private set

    private val modifiers = ArrayList<Modifier>()
    private val typeParameters = ArrayList<TypeParameter>()
    private val delegationSpecifiers = ArrayList<AnnotatedDelegationSpecifier>()
    private val typeConstraints = ArrayList<TypeConstraint>()

    fun modifiers(): Array<Modifier> =
        this.modifiers.toTypedArray()

    fun typeParameters(): Array<TypeParameter> =
        this.typeParameters.toTypedArray()

    fun delegationSpecifiers(): Array<AnnotatedDelegationSpecifier> =
        this.delegationSpecifiers.toTypedArray()

    fun typeConstraints(): Array<TypeConstraint> =
        this.typeConstraints.toTypedArray()

    override fun parse(grammarNode: GrammarNode)
    {
        this.annotation = fillModifiersAndReturnAnnotation(grammarNode[0], this.modifiers)
        this.classType = parseClassType(grammarNode[2].text)
        this.className = grammarNode[4].text
        fillTypeParameters(grammarNode[6], this.typeParameters)

        if (grammarNode[8].numberChildren > 0)
        {
            this.primaryConstructor = PrimaryConstructor()
            this.primaryConstructor?.parse(grammarNode[6][0])
        }
        else
        {
            this.primaryConstructor = null
        }

        fillDelegationSpecifiers(grammarNode[10], this.delegationSpecifiers)
        fillTypeConstraints(grammarNode[12], this.typeConstraints)
        val bodyNode = grammarNode[14][0][0]

        this.body =
            when (bodyNode.rule)
            {
                KotlinGrammar.classBody     -> ClassBody()
                KotlinGrammar.enumClassBody -> EnumBody()
                else                        -> null
            }

        this.body?.parse(bodyNode)
    }
}