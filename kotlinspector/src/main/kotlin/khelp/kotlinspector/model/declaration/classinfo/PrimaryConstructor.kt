package khelp.kotlinspector.model.declaration.classinfo

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.fillModifiersAndReturnAnnotation
import khelp.kotlinspector.model.modifier.Modifier

class PrimaryConstructor
{
    var annotation = ""
        private set
    private val modifiers = ArrayList<Modifier>()
    private val classParameters = ArrayList<ClassParameter>()

    fun modifiers(): Array<Modifier> =
        this.modifiers.toTypedArray()

    fun classParameters(): Array<ClassParameter> =
        this.classParameters.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.annotation = fillModifiersAndReturnAnnotation(grammarNode, this.modifiers)
        val classParametersNode = grammarNode[2]
        this.classParameters.clear()

        classParametersNode.forEachDeep(KotlinGrammar.classParameter) { classParameterNode ->
            val classParameter = ClassParameter()
            classParameter.parse(classParameterNode)
            this.classParameters.add(classParameter)
        }
    }
}