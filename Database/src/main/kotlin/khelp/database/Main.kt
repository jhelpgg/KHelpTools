package khelp.database

import khelp.database.condition.EQUALS
import khelp.database.condition.IN
import khelp.database.condition.NOT_EQUALS
import khelp.database.type.DataType

fun main()
{
    val database = Database.database("Test", "Test")

    val tablePerson = database.table("Person") {
        "Name" AS DataType.STRING
        "Age" AS DataType.INTEGER
    }

    val columnName = tablePerson.getColumn("Name")
    val columnAge = tablePerson.getColumn("Age")
    tablePerson.delete { }
    tablePerson.insertList {
        add {
            columnName IS "Arthur"
            columnAge IS 42
        }
        add {
            columnName IS "Joe"
            columnAge IS 73
        }
        add {
            columnName IS "My little baby"
            columnAge IS 1
        }
    }
    printDataRowResult(tablePerson.select { }, System.out)
    println("Where age = 42")
    printDataRowResult(tablePerson.select {
        +columnName
        where(columnAge EQUALS 42)
    }, System.out)
    println("Where age != 73")
    printDataRowResult(tablePerson.select {
        +columnName
        where(columnAge NOT_EQUALS 73)
    }, System.out)

    val tableNumber = database.table("Number") {
        "Number" AS DataType.INTEGER
        "Description" AS DataType.STRING
    }

    val columnNumber = tableNumber.getColumn("Number")
    val columnDescription = tableNumber.getColumn("Description")

    tableNumber.delete { }
    tableNumber.insertList {
        add {
            columnNumber IS 42
            columnDescription IS "The answer"
        }
        add {
            columnNumber IS 73
            columnDescription IS "Magic number"
        }
    }

    printDataRowResult(tableNumber.select { }, System.out)
    println("With description !")
    printDataRowResult(tablePerson.select {
        where(columnAge IN {
            select(tableNumber) {
                +columnNumber
            }
        })
    }, System.out)

    database.close()
}