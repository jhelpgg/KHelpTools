package khelp.database

import khelp.database.condition.EQUALS_ID
import khelp.database.type.DataType
import khelp.utilities.log.mark
import org.junit.jupiter.api.Test

class ForeignKeyTests
{
    @Test
    fun foreignTest()
    {
        val database = Database.database("Test", "Test", "unitTest/test1")
        val tableAddress = database.table("Address") {
            "Street" AS DataType.STRING
            "Number" AS DataType.INTEGER
        }
        val tablePerson = database.table("Person") {
            "Name" AS DataType.STRING
            "Age" AS DataType.INTEGER
            "Address" FOREIGN tableAddress
        }

        printDataRowResult(database.metadataTableOfTables.select { }, System.out)
        printDataRowResult(database.metadataTableOfTablesColumn.select { }, System.out)

        val addressJumpStreet = tableAddress.insert {
            "Street" IS "Jump Street"
            "Number" IS 21
        }

        val addressSpace = tableAddress.insert {
            "Street" IS "Space"
            "Number" IS 7777777
        }

        val personJoe = tablePerson.insert {
            "Name" IS "Joe"
            "AGE" IS 73
            "Address" IS addressJumpStreet
        }

        val personArthur = tablePerson.insert {
            "Name" IS "Arthur"
            "AGE" IS 42
            "Address" IS addressJumpStreet
        }

        val personSpaceDandy = tablePerson.insert {
            "Name" IS "Space Dandy"
            "AGE" IS 21
            "Address" IS addressSpace
        }

        mark("BEFORE")

        printDataRowResult(tablePerson.select { }, System.out)
        printDataRowResult(tableAddress.select { }, System.out)

        mark("Remove Joe")

        tablePerson.delete { where { condition = COLUMN_ID EQUALS_ID personJoe } }
        printDataRowResult(tablePerson.select { }, System.out)
        printDataRowResult(tableAddress.select { }, System.out)

        mark("Remove Space")

        tableAddress.delete { where { condition = COLUMN_ID EQUALS_ID addressSpace } }
        printDataRowResult(tablePerson.select { }, System.out)
        printDataRowResult(tableAddress.select { }, System.out)

        tableAddress.delete { }
        tablePerson.delete { }

        database.dropTable(tablePerson)
        database.dropTable(tableAddress)
        database.close()
    }

    @Test
    fun idForeignTest()
    {
        val database = Database.database("Test", "Test", "unitTest/test2")
        val tablePerson = database.table("Person") {
            "Name" AS DataType.STRING
            "Age" AS DataType.INTEGER
            "Address" AS DataType.INTEGER
        }
        val tableAddress = database.table("Address") {
            idForeign(tablePerson, "Address")
            "Street" AS DataType.STRING
            "Number" AS DataType.INTEGER
        }

        printDataRowResult(database.metadataTableOfTables.select { }, System.out)
        printDataRowResult(database.metadataTableOfTablesColumn.select { }, System.out)

        val addressJumpStreet = tableAddress.insert {
            "Street" IS "Jump Street"
            "Number" IS 21
        }

        val addressSpace = tableAddress.insert {
            "Street" IS "Space"
            "Number" IS 7777777
        }

        val personJoe = tablePerson.insert {
            "Name" IS "Joe"
            "Age" IS 73
            "Address" IS addressJumpStreet
        }

        val personArthur = tablePerson.insert {
            "Name" IS "Arthur"
            "Age" IS 42
            "Address" IS addressJumpStreet
        }

        val personSpaceDandy = tablePerson.insert {
            "Name" IS "Space Dandy"
            "Age" IS 21
            "Address" IS addressSpace
        }

        mark("BEFORE")

        printDataRowResult(tablePerson.select { }, System.out)
        printDataRowResult(tableAddress.select { }, System.out)

        mark("Remove Joe")

        tablePerson.delete { where { condition = COLUMN_ID EQUALS_ID personJoe } }
        printDataRowResult(tablePerson.select { }, System.out)
        printDataRowResult(tableAddress.select { }, System.out)

        mark("Remove Space Dandy")

        tablePerson.delete { where { condition = COLUMN_ID EQUALS_ID personSpaceDandy } }
        printDataRowResult(tablePerson.select { }, System.out)
        printDataRowResult(tableAddress.select { }, System.out)

        tableAddress.delete { }
        tablePerson.delete { }
        database.dropTable(tableAddress)
        database.dropTable(tablePerson)
        database.close()
    }
}