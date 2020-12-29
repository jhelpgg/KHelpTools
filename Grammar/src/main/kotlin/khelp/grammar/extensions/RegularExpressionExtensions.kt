package khelp.grammar.extensions

import khelp.grammar.RuleDefinitionElement
import khelp.grammar.RuleDefinitionRegularExpression
import khelp.utilities.regex.RegularExpression
import khelp.utilities.regex.WHITE_SPACE

/**
 * Rule based on regular expression with at least one white ending space
 */
val RegularExpression.WS: RuleDefinitionElement get() = RuleDefinitionRegularExpression(this + WHITE_SPACE.oneOrMore())
