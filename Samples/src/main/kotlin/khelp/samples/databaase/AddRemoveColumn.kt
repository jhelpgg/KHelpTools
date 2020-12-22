package khelp.samples.databaase

import khelp.database.Database
import khelp.database.printDataRowResult
import khelp.database.type.DataType
import khelp.utilities.log.mark

fun main()
{
    val database = Database.database("login", "password", "data/addRemoveColumns/database")
    val table = database.table("Address") {
        "Street" AS DataType.STRING
        "Number" AS DataType.INTEGER
    }

    table.insertList {
        add {
            "Street" IS "Jump street"
            "Number" IS 21
        }
        add {
            "Street" IS "Backer Street"
            "Number" IS 21
        }
    }

    mark("Original")
    printDataRowResult(database.metadataTableOfTables.select { }, System.out)
    printDataRowResult(database.metadataTableOfTablesColumn.select { }, System.out)
    printDataRowResult(table.select { }, System.out)

    mark("Add country")
    table.appendColumn("Country", DataType.STRING)
    printDataRowResult(database.metadataTableOfTables.select { }, System.out)
    printDataRowResult(database.metadataTableOfTablesColumn.select { }, System.out)
    printDataRowResult(table.select { }, System.out)

    mark("Add zip")
    table.insertColumn("Zip", ZipType.FRANCE, "Country")
    printDataRowResult(database.metadataTableOfTables.select { }, System.out)
    printDataRowResult(database.metadataTableOfTablesColumn.select { }, System.out)
    printDataRowResult(table.select { }, System.out)

    mark("Remove Zip")
    table.removeColumn("Zip")
    printDataRowResult(database.metadataTableOfTables.select { }, System.out)
    printDataRowResult(database.metadataTableOfTablesColumn.select { }, System.out)
    printDataRowResult(table.select { }, System.out)

    database.dropTable(table)
    printDataRowResult(database.metadataTableOfTables.select { }, System.out)
    printDataRowResult(database.metadataTableOfTablesColumn.select { }, System.out)
    database.close()
}