package khelp.database.query

import khelp.database.Column
import khelp.database.MatchDSL
import khelp.database.SelectDSL
import khelp.database.Table

class Match internal constructor()
{
    internal var select:Select? = null

    @SelectDSL
    fun select(table:Table, selectCreator:Select.()->Unit)
    {
        val select = Select(table)
        selectCreator(select)
        this.select = select
    }
}