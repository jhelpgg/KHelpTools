package khelp.database.exception

import khelp.database.Column
import khelp.database.Table

class TableHaveNoSuchColumnException(table: Table, column: Column) :
    Exception("Table ${table.name} have no such column : $column")