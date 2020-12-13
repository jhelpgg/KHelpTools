package khelp.database

import java.util.Objects
import khelp.database.type.DataType

data class Column internal constructor(val name: String, val type: DataType) : Comparable<Column>
{
    override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is Column)
        {
            return false
        }

        return this.name.equals(other.name, true) && this.type == other.type
    }

    override fun hashCode(): Int =
        Objects.hash(this.name.toUpperCase(), this.type)

    override operator fun compareTo(other: Column): Int =
        this.name.compareTo(other.name, true)
}

val COLUMN_ID = Column("ID", DataType.ID)

