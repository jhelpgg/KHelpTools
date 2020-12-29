package khelp.grammar.extensions

import khelp.grammar.RuleDefinitionElement
import khelp.utilities.extensions.allCharactersExcludeThis
import khelp.utilities.extensions.regularExpression

/**
 * Transform to a rule of regular expression based on char, with at least one additional white space at end
 */
val Char.WS: RuleDefinitionElement get() = this.regularExpression.WS

/**
 * Transform to a rule of regular expression based on all except a char, with at least one additional white space at end
 */
val Char.EXCLUDE_WS: RuleDefinitionElement get() = this.allCharactersExcludeThis.WS
