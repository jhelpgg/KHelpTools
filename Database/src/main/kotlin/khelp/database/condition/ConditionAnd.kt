package khelp.database.condition

import khelp.utilities.extensions.merge

/**
 * Condition valid if and only if both given conditions are valid
 */
infix fun Condition.AND(condition: Condition) =
    Condition(this.columns.merge(condition.columns), "(${this.sqlCondition}) AND (${condition.sqlCondition})")
