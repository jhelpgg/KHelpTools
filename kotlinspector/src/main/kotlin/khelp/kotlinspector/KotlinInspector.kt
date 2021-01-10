package khelp.kotlinspector

import java.io.InputStream
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.KotlinFile
import khelp.utilities.log.verbose

object KotlinInspector
{
    fun parse(fileName: String, inputStream: InputStream): KotlinFile
    {
        val grammarNode = KotlinGrammar.parse(inputStream)
                          ?: throw IllegalArgumentException("The given stream is not a valid KotlinFile")
        val kotlinFile = KotlinFile(fileName)
        kotlinFile.parse(grammarNode)
        return kotlinFile
    }
}

fun main()
{
    val kotlinFile = KotlinInspector.parse("SimpleClass",
                                           KotlinInspector::class.java.getResourceAsStream("SimpleClass.kt"))
    verbose("fileName = ", kotlinFile.fileName)
    verbose("shebangLine = ", kotlinFile.shebangLine)
    verbose("comments = ", kotlinFile.comments)
    verbose("packageName = ", kotlinFile.packageName)

    for (annotation in kotlinFile.annotations())
    {
        verbose("annotation : ", annotation)
    }

    for (importInfo in kotlinFile.imports())
    {
        verbose("import : ", importInfo)
    }

    for (topLevelObject in kotlinFile.topLevelObjects())
    {
        verbose("--***--")
        verbose(topLevelObject.comments)
        verbose(topLevelObject.declaration.declarationType)
        verbose(topLevelObject.declaration)
    }
}
