package khelp.database.query

import khelp.database.InsertDSL
import khelp.database.Table

class InsertList internal constructor(val table:Table)
{
    @InsertDSL
    fun add(insertCreator:Insert.()->Unit)
    {
        this.table.insert(insertCreator)
    }
}