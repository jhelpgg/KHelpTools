package khelp.database.condition

import khelp.utilities.extensions.merge

infix fun Condition.OR(condition: Condition) =
    Condition(this.columns.merge(condition.columns), "(${this.sqlCondition}) OR (${condition.sqlCondition})")
