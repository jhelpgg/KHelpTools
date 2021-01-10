package khelp.kotlinspector.model

import khelp.grammar.GrammarNode

class KotlinFile internal constructor(val fileName: String)
{
    var shebangLine: String = ""
        private set
    var comments: String = ""
        private set
    var packageName: String = ""
        private set

    private val annotations = ArrayList<String>()
    private val imports = ArrayList<KotlinImport>()
    private val topLevelObjects = ArrayList<TopLevelObject>()

    fun annotations(): Array<String> =
        this.annotations.toTypedArray()

    fun imports(): Array<KotlinImport> =
        this.imports.toTypedArray()

    fun topLevelObjects(): Array<TopLevelObject> =
        this.topLevelObjects.toTypedArray()

    internal fun parse(grammarNode: GrammarNode)
    {
        this.shebangLine = grammarNode[0].text
        this.annotations.clear()

        for (annotationNode in grammarNode[2])
        {
            this.annotations.add(annotationNode.text)
        }

        this.comments = grammarNode[4].text
        val packageHeaderNode = grammarNode[6]
        this.packageName = packageHeaderNode[2].text
        this.imports.clear()

        for (importHeader in grammarNode[8])
        {
            val comments = importHeader[0].text
            val importPath = importHeader[4].text + importHeader[6].text
            this.imports.add(KotlinImport(importPath, comments))
        }

        this.topLevelObjects.clear()

        for (topLevelObjectNode in grammarNode[10])
        {
            val topLevelObject = TopLevelObject()
            topLevelObject.parse(topLevelObjectNode)
            this.topLevelObjects.add(topLevelObject)
        }
    }
}