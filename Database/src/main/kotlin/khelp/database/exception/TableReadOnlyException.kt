package khelp.database.exception

import khelp.database.Table

/**
 * Throws if tries to modifiy a read only table
 */
class TableReadOnlyException(table:Table) : Exception("The table${table.name} is read only")
