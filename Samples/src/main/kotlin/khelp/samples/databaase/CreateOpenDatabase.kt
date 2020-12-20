package khelp.samples.databaase

import khelp.database.Database

fun main()
{
    // Create or open the database
    val database = Database.database("login", "password", "data/createOpen/database")

    // TODO Manipulate the database

    // Always close properly the database before exit
    database.close()
}
