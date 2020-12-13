package khelp.database.condition

fun not(condition: Condition) =
    Condition(condition.columns, "NOT(${condition.sqlCondition})")
