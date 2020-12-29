package khelp.grammar.extensions

import khelp.grammar.RuleDefinitionElement
import khelp.utilities.extensions.regularExpression

/**
 * Transform to a rule of regular expression based on String, with at least one additional white space at end
 */
val String.WS: RuleDefinitionElement get() = this.regularExpression.WS
