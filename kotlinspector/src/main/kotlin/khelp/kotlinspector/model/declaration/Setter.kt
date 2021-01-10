package khelp.kotlinspector.model.declaration

import khelp.grammar.GrammarNode
import khelp.kotlinspector.fillModifiersAndReturnAnnotation
import khelp.kotlinspector.model.declaration.classinfo.FunctionBody
import khelp.kotlinspector.model.modifier.Modifier
import khelp.kotlinspector.model.type.Type

class Setter
{
    var annotation = ""
        private set
    var parameterWithOptionalType : ParameterWithOptionalType?=null
    private set
    var type : Type?=null
        private set
    var functionBody : FunctionBody?= null
        private set


    private val modifiers = ArrayList<Modifier>()

    fun modifiers() : Array<Modifier> =
        this.modifiers.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.parameterWithOptionalType=null
        this.type = null
        this.functionBody = null
        val node = grammarNode[0]
        this.annotation = fillModifiersAndReturnAnnotation(node[0], this.modifiers)

        if(node.numberChildren>3)
        {
             this.parameterWithOptionalType = ParameterWithOptionalType()
            this.parameterWithOptionalType?.parse(node[6])

            val typeNode = node[12]

            if(typeNode.numberChildren>0)
            {
                this.type = Type()
                this.type?.parse(typeNode[2])
            }

            this.functionBody = FunctionBody()
            this.functionBody?.parse(node[14])
        }
    }
}