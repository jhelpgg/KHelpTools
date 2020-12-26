package khelp.database.condition

import khelp.database.Column
import khelp.database.MatchDSL
import khelp.database.query.Match
import khelp.utilities.argumentCheck

/**
 * Create condition that select rows, in given column, wih values match the result of given selection
 *
 * See documentation for match DSL syntax
 */
@MatchDSL
infix fun Column.IN(matchCreator: Match.() -> Unit): Condition
{
    val match = Match()
    matchCreator(match)
    val select = match.select ?: throw IllegalStateException("Must select something")
    argumentCheck(select.numberColumns == 1) { "Select must return one and only one column" }
    val selectType = select[0].type
    argumentCheck(this.type.compatible(selectType)) { "Column with data type ${this.type} not compatible with return select type $selectType" }
    return Condition(arrayOf(this), "${this.name} IN (${select.selectSQL()})")
}
