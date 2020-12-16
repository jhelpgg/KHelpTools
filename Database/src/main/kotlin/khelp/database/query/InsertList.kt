package khelp.database.query

import khelp.database.InsertDSL
import khelp.database.Table

/**
 * For insert several rows in one time
 *
 * See documentation about insert list DSL syntax
 */
class InsertList internal constructor(val table:Table)
{
    /**
     * Add row to insert
     */
    @InsertDSL
    fun add(insertCreator:Insert.()->Unit)
    {
        this.table.insert(insertCreator)
    }
}