package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.utilities.log.mark
import org.junit.jupiter.api.Test

class NavigationSuffixTests
{
    @Test
    fun invokeMethod()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("?.close()", KotlinGrammar.navigationSuffix))
mark("dop")
        println(grammarNode)
//        TODO()
    }

    @Test
    fun removeMe()
    {
        //val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("inputStream?.close()", KotlinGrammar.directlyAssignableExpression))
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("inputStream?.close()", KotlinGrammar.postfixUnaryExpression))
mark("dop")
        println(grammarNode)
    }
}
