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
