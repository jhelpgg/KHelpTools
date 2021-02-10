package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.UnaryPrefix
import khelp.kotlinspector.model.expression.operator.PrefixUnaryOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UnaryPrefixTests
{
    @Test
    fun annotation()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("@JvmOverloads",
                                                                        KotlinGrammar.unaryPrefix))
        val unaryPrefix = UnaryPrefix()
        unaryPrefix.parse(grammarNode)
        Assertions.assertEquals("@JvmOverloads", unaryPrefix.annotation)
    }

    @Test
    fun label()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("person@",
                                                                        KotlinGrammar.unaryPrefix))
        val unaryPrefix = UnaryPrefix()
        unaryPrefix.parse(grammarNode)
        Assertions.assertEquals("person@", unaryPrefix.label)
    }

    @Test
    fun prefixUnaryOperator()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("++",
                                                                        KotlinGrammar.unaryPrefix))
        var unaryPrefix = UnaryPrefix()
        unaryPrefix.parse(grammarNode)
        Assertions.assertEquals(PrefixUnaryOperator.INCREMENT, unaryPrefix.prefixUnaryOperator)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("--",
                                                                    KotlinGrammar.unaryPrefix))
        unaryPrefix = UnaryPrefix()
        unaryPrefix.parse(grammarNode)
        Assertions.assertEquals(PrefixUnaryOperator.DECREMENT, unaryPrefix.prefixUnaryOperator)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("!",
                                                                    KotlinGrammar.unaryPrefix))
        unaryPrefix = UnaryPrefix()
        unaryPrefix.parse(grammarNode)
        Assertions.assertEquals(PrefixUnaryOperator.NOT, unaryPrefix.prefixUnaryOperator)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("+",
                                                                    KotlinGrammar.unaryPrefix))
        unaryPrefix = UnaryPrefix()
        unaryPrefix.parse(grammarNode)
        Assertions.assertEquals(PrefixUnaryOperator.UNARY_PLUS, unaryPrefix.prefixUnaryOperator)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("-",
                                                                    KotlinGrammar.unaryPrefix))
        unaryPrefix = UnaryPrefix()
        unaryPrefix.parse(grammarNode)
        Assertions.assertEquals(PrefixUnaryOperator.UNARY_MINUS, unaryPrefix.prefixUnaryOperator)
    }
}