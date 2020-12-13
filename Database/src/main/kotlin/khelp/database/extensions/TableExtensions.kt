package khelp.database.extensions

import khelp.database.Column
import khelp.database.Table
import khelp.database.exception.TableHaveNoSuchColumnException
import khelp.database.exception.TableReadOnlyException

fun Table.checkColumn(column: Column)
{
    if (column !in this)
    {
        throw TableHaveNoSuchColumnException(this, column)
    }
}

fun Table.checkReadOnly()
{
    if (this.readOnly)
    {
        throw TableReadOnlyException(this)
    }
}