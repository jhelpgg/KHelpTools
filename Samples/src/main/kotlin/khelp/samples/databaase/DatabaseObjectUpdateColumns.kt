package khelp.samples.databaase

import khelp.database.Database
import khelp.database.databaseobject.DatabaseObject
import khelp.database.printDataRowResult
import khelp.utilities.log.mark

fun main()
{
    val database = Database.database("login", "password", "data/database_object_update/database")
    mark("Before")
    printDataRowResult(DatabaseObject.table(database, Address2::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Person2::class.java)
                           .select { }, System.out)

    Person2("Hello",
            42,
            Address2("Street", 21, database).waitCreated(),
            database).waitCreated<Person>()
    Person2("Arthur",
            42,
            Address2("Street", 21, database).waitCreated(),
            database).waitCreated<Person>()
    Person2("Joe",
            73,
            Address2("Street", 21, database).waitCreated(),
            database).waitCreated<Person>()
    Person2("Space Dandy",
            21,
            Address2("Space", 7777777, database).waitCreated(),
            database).waitCreated<Person>()
    Person2("John",
            45,
            Address2("Street", 21, database).waitCreated(), database).waitCreated<Person>()
    printDataRowResult(database.metadataTableOfTables.select { }, System.out)
    printDataRowResult(database.metadataTableOfTablesColumn.select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Person2::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Address2::class.java)
                           .select { }, System.out)
    database.close()
}