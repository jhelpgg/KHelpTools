package khelp.database

import khelp.database.type.DataType

fun main()
{
    val database = Database.database("Test", "Test")

    val tablePerson = database.table("Person") {
        "Name" AS DataType.STRING
        "Age" AS DataType.INTEGER
    }

    tablePerson.delete { }
    tablePerson.insertList {
        add {
            "Name" IS "Arthur"
            "Age" IS 42
        }
        add {
            "Name" IS "Joe"
            "Age" IS 73
        }
        add {
            "Name" IS "My little baby"
            "Age" IS 1
        }
    }
    printDataRowResult(tablePerson.select { }, System.out)
    println("Where age = 42")
    printDataRowResult(tablePerson.select {
        +"Name"
        where { condition = "Age" EQUALS 42 }
    }, System.out)
    println("Where age != 73")
    printDataRowResult(tablePerson.select {
        +"Name"
        where { condition = "Age" NOT_EQUALS 73 }
    }, System.out)

    val tableNumber = database.table("Number") {
        "Number" AS DataType.INTEGER
        "Description" AS DataType.STRING
    }

    tableNumber.delete { }
    tableNumber.insertList {
        add {
            "Number" IS 42
            "Description" IS "The answer"
        }
        add {
            "Number" IS 73
            "Description" IS "Magic number"
        }
    }

    printDataRowResult(tableNumber.select { }, System.out)
    println("With description !")
    printDataRowResult(tablePerson.select {
        where {
            condition = "Age" IN {
                select(tableNumber) {
                    +"Number"
                }
            }
        }
    }, System.out)

    database.close()
}