package khelp.database.exception

import khelp.database.Table

class TableReadOnlyException(table:Table) : Exception("The table${table.name} is read only")
