package khelp.samples.databaase

import khelp.database.Database
import khelp.database.condition.AND
import khelp.database.databaseobject.DatabaseObject
import khelp.database.printDataRowResult
import khelp.utilities.log.debug
import khelp.utilities.log.mark

fun main()
{
    val database = Database.database("login", "password", "data/database_object/database")
    mark("Before")
    printDataRowResult(DatabaseObject.table(database, Address::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Person::class.java)
                           .select { }, System.out)

    Person("Hello",
           42,
           Address("Street", 21, database).waitCreated(),
           database).waitCreated<Person>()
    Person("Arthur",
           42,
           Address("Street", 21, database).waitCreated(),
           database).waitCreated<Person>()
    val joe = Person("Joe",
                     73,
                     Address("Street", 21, database).waitCreated(),
                     database).waitCreated<Person>()
    val dandy = Person("Space Dandy",
                       21,
                       Address("Space", 7777777, database).waitCreated(),
                       database).waitCreated<Person>()
    Person("John",
           45,
           Address("Street", 21, database).waitCreated(),
           database).waitCreated<Person>()
    printDataRowResult(database.metadataTableOfTables.select { }, System.out)
    printDataRowResult(database.metadataTableOfTablesColumn.select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Person::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Address::class.java)
                           .select { }, System.out)

    val result = DatabaseObject.select<Person>(database) {
        condition = (Person::age UPPER 40) AND (Person::age LOWER 50)
    }
    while (result.hasNext)
    {
        val p = result.next()
        debug(">> ${p.name} : ${p.age} : ${p.address.street} : ${p.address.number}")
    }
    result.close()

    mark("REMOVE")
    val nb = DatabaseObject.delete<Person>(database) {
        condition = Person::age EQUALS 42
    }

    mark("nb=$nb")

    val done = DatabaseObject.delete(dandy)
    mark("done=$done")

    printDataRowResult(DatabaseObject.table(database, Person::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Address::class.java)
                           .select { }, System.out)

    mark("Change Joe")
    joe.age = 37
    joe.address = Address("Somewhere", 12, database).waitCreated()
    printDataRowResult(DatabaseObject.table(database, Person::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Address::class.java)
                           .select { }, System.out)


    database.close()
}