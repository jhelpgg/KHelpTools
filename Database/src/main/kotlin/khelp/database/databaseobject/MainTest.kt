package khelp.database.databaseobject

import khelp.database.Database
import khelp.database.condition.AND
import khelp.database.printDataRowResult
import khelp.utilities.log.debug
import khelp.utilities.log.mark

fun main()
{
    val database = Database.database("l", "p", "data/t")
    mark("Before")
    printDataRowResult(DatabaseObject.table(database, TestAddress::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, TestPerson::class.java)
                           .select { }, System.out)

    TestPerson("Hello",
               42,
               TestAddress("Street", 21, database).waitCreated(),
               database).waitCreated<TestPerson>()
    TestPerson("Arthur",
               42,
               TestAddress("Street", 21, database).waitCreated(),
               database).waitCreated<TestPerson>()
    val joe = TestPerson("Joe",
                         73,
                         TestAddress("Street", 21, database).waitCreated(),
                         database).waitCreated<TestPerson>()
    val dandy = TestPerson("Space Dandy",
                           21,
                           TestAddress("Space", 7777777, database).waitCreated(),
                           database).waitCreated<TestPerson>()
    TestPerson("John",
               45,
               TestAddress("Street", 21, database).waitCreated(),
               database).waitCreated<TestPerson>()
    printDataRowResult(database.metadataTableOfTables.select { }, System.out)
    printDataRowResult(database.metadataTableOfTablesColumn.select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, TestPerson::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, TestAddress::class.java)
                           .select { }, System.out)

    val result = DatabaseObject.select<TestPerson>(database) {
        condition = (TestPerson::age UPPER 40) AND (TestPerson::age LOWER 50)
    }
    while (result.hasNext)
    {
        val p = result.next()
        debug(">> ${p.name} : ${p.age} : ${p.address.street} : ${p.address.number}")
    }
    result.close()

    mark("REMOVE")
    val nb = DatabaseObject.delete<TestPerson>(database) {
        condition = TestPerson::age EQUALS 42
    }

    mark("nb=$nb")

    val done = DatabaseObject.delete(dandy)
    mark("done=$done")

    printDataRowResult(DatabaseObject.table(database, TestPerson::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, TestAddress::class.java)
                           .select { }, System.out)

    mark("Change Joe")
    joe.age = 37
    joe.address = TestAddress("Somewhere", 12, database).waitCreated()
    printDataRowResult(DatabaseObject.table(database, TestPerson::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, TestAddress::class.java)
                           .select { }, System.out)


    database.close()
}