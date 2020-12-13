package khelp.database.condition

import khelp.utilities.extensions.merge

infix fun Condition.AND(condition: Condition) =
    Condition(this.columns.merge(condition.columns), "(${this.sqlCondition}) AND (${condition.sqlCondition})")
