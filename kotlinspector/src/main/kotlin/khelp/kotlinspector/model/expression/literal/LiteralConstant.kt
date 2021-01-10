package khelp.kotlinspector.model.expression.literal

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class LiteralConstant
{
    var literalConstantType = LiteralConstantType.NULL
        private set
    var value = ""
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.literalConstantType =
            when (grammarNode[0].rule)
            {
                KotlinGrammar.BooleanLiteral   -> LiteralConstantType.BOOLEAN
                KotlinGrammar.IntegerLiteral   -> LiteralConstantType.INTEGER
                KotlinGrammar.HexLiteral       -> LiteralConstantType.HEXADECIMAL
                KotlinGrammar.BinLiteral       -> LiteralConstantType.BINARY
                KotlinGrammar.CharacterLiteral -> LiteralConstantType.CHARACTER
                KotlinGrammar.RealLiteral      -> LiteralConstantType.REAL
                KotlinGrammar.LongLiteral      -> LiteralConstantType.LONG
                KotlinGrammar.UnsignedLiteral  -> LiteralConstantType.UNSIGNED
                else                           -> LiteralConstantType.NULL
            }

        this.value = grammarNode.text
    }
}