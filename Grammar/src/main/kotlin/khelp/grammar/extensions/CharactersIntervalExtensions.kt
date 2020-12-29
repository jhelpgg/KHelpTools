package khelp.grammar.extensions

import khelp.grammar.RuleDefinitionElement
import khelp.utilities.extensions.allCharactersExcludeThose
import khelp.utilities.extensions.regularExpression
import khelp.utilities.text.CharactersInterval

/**
 * Transform to a rule of regular expression based on interval, with at least one additional white space at end
 */
val CharactersInterval.WS: RuleDefinitionElement get() = this.regularExpression.WS

/**
 * Transform to a rule of regular expression based on all except interval, with at least one additional white space at end
 */
val CharactersInterval.EXCLUE_WS: RuleDefinitionElement get() = this.allCharactersExcludeThose.WS
