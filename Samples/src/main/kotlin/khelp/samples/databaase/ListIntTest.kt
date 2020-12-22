package khelp.samples.databaase

import khelp.database.Database
import khelp.database.databaseobject.DatabaseObject
import khelp.database.printDataRowResult
import khelp.database.type.DataType
import khelp.utilities.extensions.string

fun main()
{
    val database = Database.database("login", "password", "data/listInt/database")
    val table = database.table("Test") {
        "Name" AS DataType.STRING
        "List" AS DataType.INT_ARRAY
    }

    printDataRowResult(table.select { }, System.out)

    table.insert {
        "Name" IS "TER"
        "List" IS intArrayOf(1, 2, 5, 8, 9, 3578624, -9510)
    }

    printDataRowResult(table.select { }, System.out)

    val result = table.select { }

    if (result.hasNext)
    {
        result.next {
            println("1) ${getID(1)}")
            println("2) ${getString(2)}")
            println("3) ${getIntArray(3).string()}")
        }
    }

    result.close()

    WithListInt(42, intArrayOf(73, 666, 987654321, -75369842), database).waitCreated<WithListInt>()
    printDataRowResult(DatabaseObject.table(database, WithListInt::class.java)
                           .select { }, System.out)

    val withListIntResult = DatabaseObject.select<WithListInt>(database) {}

    while (withListIntResult.hasNext)
    {
        val w = withListIntResult.next()
        println(w.numberKey)
        println(w.arrayOfInt.string())
    }

    withListIntResult.close()

    AddressBook("Secret",
                arrayOf(Address("Jump Street", 21, database).waitCreated(),
                        Address("Backer Street", 21, database).waitCreated()),
                database)
        .waitCreated<AddressBook>()
    printDataRowResult(DatabaseObject.table(database, AddressBook::class.java)
                           .select { }, System.out)

    val addresses = DatabaseObject.select<AddressBook>(database) {}

    while (addresses.hasNext)
    {
        val w = addresses.next()
        println(w.name)
        println(w.addresses.string())
    }

    addresses.close()

    printDataRowResult(DatabaseObject.table(database, Address::class.java)
                           .select { }, System.out)

    database.close()
}