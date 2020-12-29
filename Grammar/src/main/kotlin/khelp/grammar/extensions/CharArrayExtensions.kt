package khelp.grammar.extensions

import khelp.grammar.RuleDefinitionElement
import khelp.utilities.extensions.allCharactersExcludeThose
import khelp.utilities.extensions.regularExpression

/**
 * Transform to a rule of regular expression based on char array, with at least one additional white space at end
 */
val CharArray.WS: RuleDefinitionElement get() = this.regularExpression.WS

/**
 * Transform to a rule of regular expression based on all except char array, with at least one additional white space at end
 */
val CharArray.EXCLUDE_WS: RuleDefinitionElement get() = this.allCharactersExcludeThose.WS
