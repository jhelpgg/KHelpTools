package khelp.samples.databaase

import khelp.database.Database
import khelp.database.type.DataType

fun main()
{
    // Create or open the database
    val database = Database.database("login", "password", "data/createTable/database")

    val tablePerson = database.table("Person") {
        "Name" AS DataType.STRING
        "Birthdate" AS DataType.CALENDAR
    }

    // Show table columns
    for (column in tablePerson)
    {
        println(column)
    }

    tablePerson.delete { where { condition = "Name" ONE_OF arrayOf("John Doe", "Arthur", "Skwwek") } }
    // Always close properly the database before exit
    database.close()
}
