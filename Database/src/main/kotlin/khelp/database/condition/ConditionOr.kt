package khelp.database.condition

import khelp.utilities.extensions.merge

/**
 * Condition valid if at least one of given condition is valid
 */
infix fun Condition.OR(condition: Condition) =
    Condition(this.columns.merge(condition.columns), "(${this.sqlCondition}) OR (${condition.sqlCondition})")
