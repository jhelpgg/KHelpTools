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
