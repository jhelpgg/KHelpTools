package khelp.samples.databaase

import khelp.database.Database
import khelp.database.printDataRowResult
import khelp.database.type.DataType
import khelp.utilities.log.mark

enum class DeviceType
{
    COMPUTER,
    DESKTOP,
    PHONE
}

enum class OS
{
    LINUX,
    WINDOWS,
    MAC,
    ANDROID,
    IOS
}

const val COLUMN_NAME = "Name"
const val COLUMN_TYPE = "Device_Type"
const val COLUMN_OS = "OS"
const val COLUMN_MODEL = "Model"
const val COLUMN_VERSION = "Version"

fun main()
{
    // Create or open the database
    val database = Database.database("login", "password", "data/selectPrint/database")

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
        +COLUMN_MODEL
        +COLUMN_OS
    }
    printDataRowResult(result, System.out)

    println()
    mark("Model and OS filtered")
    result = tableDevice.select {
        +COLUMN_MODEL
        +COLUMN_OS
        where { condition = COLUMN_VERSION EQUALS 11 }
    }
    printDataRowResult(result, System.out)

    mark("All table order name ascendant")
    result = tableDevice.select {
        ascendant(COLUMN_NAME)
    }
    printDataRowResult(result, System.out)

    println()
    mark("Model and OS descendant model")
    result = tableDevice.select {
        +COLUMN_MODEL
        +COLUMN_OS
        descendant(COLUMN_MODEL)
    }
    printDataRowResult(result, System.out)

    println()
    mark("Model and OS descendant model")
    result = tableDevice.select {
        descendant(COLUMN_MODEL)
    }
    printDataRowResult(result, System.out)

    // Always close properly the database before exit
    database.close()
}
