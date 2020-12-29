package khelp.grammar.prebuilt

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import khelp.grammar.Grammar
import khelp.grammar.GrammarNode
import khelp.utilities.extensions.plus
import khelp.utilities.extensions.zeroOrMore
import khelp.utilities.regex.ANY
import khelp.utilities.regex.NAME

const val PROPERTIES_RULE_PROPERTIES = "PROPERTIES"
const val PROPERTIES_RULE_LINE = "LINE"
const val PROPERTIES_RULE_WHITE_SPACE = "WHITE_SPACE"
const val PROPERTIES_RULE_PROPERTY_LINE = "PROPERTY_LINE"
const val PROPERTIES_RULE_END_LINE = "END_LINE"
const val PROPERTIES_RULE_DEFINITION = "DEFINITION"
const val PROPERTIES_RULE_COMMENT = "COMMENT"
const val PROPERTIES_RULE_NAME = "NAME"
const val PROPERTIES_RULE_VALUE = "VALUE"

object Properties
{
    private val grammar = Grammar()

    init
    {
        this.grammar.rules {
            PROPERTIES_RULE_PROPERTIES IS { rule = PROPERTIES_RULE_LINE.zeroOrMore() }
            PROPERTIES_RULE_LINE IS { rule = PROPERTIES_RULE_WHITE_SPACE * PROPERTIES_RULE_PROPERTY_LINE * PROPERTIES_RULE_END_LINE }
            PROPERTIES_RULE_WHITE_SPACE IS { rule = +charArrayOf(' ', '\t').zeroOrMore() }
            PROPERTIES_RULE_END_LINE IS { rule = +(charArrayOf(' ', '\t').zeroOrMore() + '\n') }
            PROPERTIES_RULE_PROPERTY_LINE IS { rule =  PROPERTIES_RULE_DEFINITION I PROPERTIES_RULE_COMMENT I PROPERTIES_RULE_WHITE_SPACE }
            PROPERTIES_RULE_DEFINITION IS {
                rule = PROPERTIES_RULE_NAME *
                       (charArrayOf(' ', '\t').zeroOrMore() + '=' + charArrayOf(' ', '\t').zeroOrMore()) *
                       PROPERTIES_RULE_VALUE
            }
            PROPERTIES_RULE_NAME IS { rule = +NAME }
            PROPERTIES_RULE_VALUE IS { rule = +ANY.zeroOrMore() }
            PROPERTIES_RULE_COMMENT IS { rule = +('#' + ANY.zeroOrMore()) }
        }
    }

    fun parse(inputStream: InputStream): GrammarNode?
    {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line = bufferedReader.readLine()

        while (line != null)
        {
            stringBuilder.append(line)
            stringBuilder.append('\n')
            line = bufferedReader.readLine()
        }

        bufferedReader.close()
        return this.grammar.parse(stringBuilder.toString())
    }
}