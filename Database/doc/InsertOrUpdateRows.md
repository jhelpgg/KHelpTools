# Insert or update data

1. [Insert one row](#insert-one-row)
1. [Insert or update one row](#insert-or-update-one-row)
1. [Insert several rows](#insert-several-rows)
1. [Update several rows](#update-several-rows)

Here instructions to add or modify rows in a table.

## Insert one row

To insert one row in a table, use `insert` method from `Table`

```kotlin
package khelp.samples.databaase

import java.util.Calendar
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

    val date = Calendar.getInstance()
    date.set(1985,Calendar.APRIL, 1)
    
    tablePerson.insert {
        "Name" IS "Arthur"
        "Birthdate" IS date
    }

    tablePerson.delete { where { condition = "Name" ONE_OF arrayOf("John Doe", "Arthur", "Skwwek") } }
    // Always close properly the database before exit
    database.close()
}
```

Before the `IS` its the column name or column instance, after is the value to given to the column

If not all table columns are specified, the system put a default value for them. Except for `enum` that throw an exception, if no values specifies for the column.

The value must be the same type as column support. See [Data types](ColumnsAndTable.md#data-types) for a list of data type.

## Insert or update one row

The insert, have possibility to specifies a condition. If the condition match to exctly one row. 
No insert is done, but the corresponding row is modified with given values.

```kotlin
package khelp.samples.databaase

import java.util.Calendar
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

    val date = Calendar.getInstance()
    date.set(1985, Calendar.APRIL, 1)

    tablePerson.insert {
        "Name" IS "Arthur"
        "Birthdate" IS date
        updateIfExactlyOneRowMatch { condition = "Name" EQUALS "Arthur" }
    }

    tablePerson.delete { where { condition = "Name" ONE_OF arrayOf("John Doe", "Arthur", "Skwwek") } }
    // Always close properly the database before exit
    database.close()
}
```

For knows more about specify condition : [Where and conditions](WhereAndConditions.md)

## Insert several rows

It is possible to add several rows in a time

```kotlin
package khelp.samples.databaase

import java.util.Calendar
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

    val date = Calendar.getInstance()
    date.set(1985, Calendar.APRIL, 1)
    val date2 = Calendar.getInstance()
    date2.set(2001, Calendar.JANUARY, 1)
    val date3 = Calendar.getInstance()
    date3.set(1970, Calendar.JANUARY, 1)

    tablePerson.insertList {
        add {
            "Name" IS "Arthur"
            "Birthdate" IS date
        }
        add {
            "Name" IS "Bachelet"
            "Birthdate" IS date2
        }
        add {
            "Name" IS "World"
            "Birthdate" IS date3
        }
    }

    tablePerson.delete { where { condition = "Name" ONE_OF arrayOf("John Doe", "Arthur", "Skwwek") } }
    // Always close properly the database before exit
    database.close()
}
```

Each `add` have the same syntax as `insert`

## Update several rows

To modify some rows :

```kotlin
package khelp.samples.databaase

import java.util.Calendar
import java.util.regex.Pattern
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

    val date = Calendar.getInstance()
    date.set(1985,Calendar.APRIL, 1)
    val patternStartWithA = Pattern.compile("[aA].*")

    tablePerson.update {
        "Birthdate" IS date
        where { condition = "Name" REGEX patternStartWithA }
    }

    tablePerson.delete { where { condition = "Name" ONE_OF arrayOf("John Doe", "Arthur", "Skwwek") } }
    // Always close properly the database before exit
    database.close()
}
```

Like insert, specify neq colmum values with `IS`. 
This time, if a column of the table not specified, the column value not change.

The `where` specifies row to change. If not specified, it will modify the entire table.

More about conditions :  [Where and conditions](WhereAndConditions.md)

[Menu](Menu.md)
