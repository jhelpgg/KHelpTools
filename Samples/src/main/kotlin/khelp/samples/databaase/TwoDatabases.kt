package khelp.samples.databaase

import khelp.database.Database
import khelp.database.printDataRowResult
import khelp.database.type.DataType
import khelp.utilities.log.mark

fun main()
{
    val database1 = Database.database("login1", "password1", "data/twoDatabases/database1")
    val database2 = Database.database("login2", "password2", "data/twoDatabases/database2")

    var tableDevice = database1.table("Device") {
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
    database1.dropTable(tableDevice)

    mark("Other database")

    tableDevice = database2.table("Device") {
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
    result = tableDevice.select { }
    printDataRowResult(result, System.out)
    database2.dropTable(tableDevice)

    database1.close()
    database2.close()
}