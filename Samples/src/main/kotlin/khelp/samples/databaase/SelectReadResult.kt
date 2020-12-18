package khelp.samples.databaase

import khelp.database.Database
import khelp.database.printDataRowResult
import khelp.database.type.DataType
import khelp.utilities.log.mark

fun main()
{
    // Create or open the database
    val database = Database.database("login", "password")

    val tableDevice = database.table("Device") {
        COLUMN_NAME AS DataType.STRING
        COLUMN_TYPE AS DataType.ENUM
        COLUMN_OS AS DataType.ENUM
        COLUMN_MODEL AS DataType.STRING
        COLUMN_VERSION AS DataType.INTEGER
    }

    tableDevice.insertList {
        add {
            COLUMN_NAME IS "Pixel 3"
            COLUMN_TYPE IS DeviceType.PHONE
            COLUMN_OS IS OS.ANDROID
            COLUMN_MODEL IS "HammerHead"
            COLUMN_VERSION IS 11
        }
        add {
            COLUMN_NAME IS "IPhone9"
            COLUMN_TYPE IS DeviceType.PHONE
            COLUMN_OS IS OS.IOS
            COLUMN_MODEL IS "IPhone9"
            COLUMN_VERSION IS 9
        }
        add {
            COLUMN_NAME IS "MyComputer"
            COLUMN_TYPE IS DeviceType.COMPUTER
            COLUMN_OS IS OS.LINUX
            COLUMN_MODEL IS "Ubuntu"
            COLUMN_VERSION IS 11
        }
        add {
            COLUMN_NAME IS "Work"
            COLUMN_TYPE IS DeviceType.DESKTOP
            COLUMN_OS IS OS.LINUX
            COLUMN_MODEL IS "Ubuntu"
            COLUMN_VERSION IS 13
        }
    }

    mark("All table")
    var result = tableDevice.select { }
    printDataRowResult(result, System.out)

    println()
    mark("Model and OS")
    result = tableDevice.select {
        +COLUMN_OS
        +COLUMN_MODEL
    }

    while (result.hasNext)
    {
        result.next {
            print("> ")
            print(getEnum<OS>(1))
            print(" : ")
            println(getString(2))
        }
    }

    result.close()

    database.dropTable(tableDevice)

    // Always close properly the database before exit
    database.close()
}
