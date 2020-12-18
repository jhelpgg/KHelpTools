# Create table if not exists

1. [Data types](#data-types)
1. [Table creation](#table-creation)

A table has a name and composed of columns describes by their name and type of data they carry.

Tables will have automatic, has first column, an **ID** for each row that is an integer used as the primary key.

## Data types

Data types are defines in enumeration `khelp.database.type.DataType`

| Type | Description |
|:-----:|-----|
| `ID` | Special type for primary key |
| `STRING` | Column contains **String** limited to **8 192** characters |
| `BOOLEAN` | Column contains **Boolean** |
| `BYTE` | Column contains **Byte** |
| `SHORT` | Column contains **Short** |
| `INTEGER` | Column contains **Int** |
| `LONG` | Column contains **Long** |
| `FLOAT` | Column contains **Float** |
| `DOUBLE` | Column contains **Double** |
| `BYTE_ARRAY` | Column contains **ByteArray**. The array size is limited to **12 228** bytes |
| `CALENDAR` | Column contains **java.util.Calendar** | 
| `DATE` | Column contains **khelp.database.type.DataDate**. It represents a day defines by its year, month and day in month |
| `TIME` | Column contains **khelp.database.type.DataTime**. It represents a time passed in hours, minutes, seconds and milliseconds |
| `ENUM` | Column contains an `enum`. It is developer responsibility to know witch `enum` type is stored. When read the database will tries to cast in requested type by the developer. |

Data types aren't compatible to each other. Just one exception, **ID** can match with **INTEGER**

## Table creation

Table have unique name. Names are case-insensitive. It means **Person**, **PERSON** and **person**, for instance, are
the same name.

Inside a table, two columns can't have same names. Names are also case-insensitive.

To create a table we use the method **table** of **Database**. If the table already exists, it is just returns

The column description is done by **DSL** syntax.

Example:

```kotlin
package khelp.samples.databaase

import khelp.database.Database
import khelp.database.type.DataType

fun main()
{
    // Create or open the database
    val database = Database.database("login", "password")

    val tablePerson = database.table("Person") {
        "Name" AS DataType.STRING
        "Birthdate" AS DataType.CALENDAR
    }

    // Show table columns
    for (column in tablePerson)
    {
        println(column)
    }

    // Always close properly the database before exit
    database.close()
}
```

That prints:

```shell
Column(name=ID, type=ID)
Column(name=Name, type=STRING)
Column(name=Birthdate, type=CALENDAR)
```

As we said, the primary key **ID** is automatically created as first column.

To create a column, just specify its name followed by the keyword **AS** and then the column type.

[Menu](Menu.md)
