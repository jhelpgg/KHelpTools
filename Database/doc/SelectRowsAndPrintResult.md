# Select values and exploit the result

1. [The selection](#the-selection)
1. [Exploit the result](#exploit-the-result)

Here will explain how read data from the database.

## The selection

It is possible to choose columns from the table that inside in the result, and their order they will be presented.  
If no columns are selected, it will select all table columns in the order they are declared for the table.
If need only some columns, and want to  ignore other, it is better for performance and memory to select only required columns.

```kotlin
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
            COLUMN_VERSION IS 11
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

    // Always close properly the database before exit
    database.close()
}
```

That prints

```shell
2020-12-18 07:33:11:236 INFORMATION khelp.samples.databaase.SelectPrintKt.main at 74: *=> MARK
*********************
***   All table   ***
*********************
+---------------------------------------------------------------------------------------------------------------------------+
|                                                          Device                                                           |
+----+------------+---------------------------------------------+------------------------------------+------------+---------+
| ID |    Name    |                 Device_Type                 |                 OS                 |   Model    | Version |
+----+------------+---------------------------------------------+------------------------------------+------------+---------+
| 1  |  Pixel 3   |  khelp.samples.databaase.DeviceType:PHONE   | khelp.samples.databaase.OS:ANDROID | HammerHead |   11    |
| 2  |  IPhone9   |  khelp.samples.databaase.DeviceType:PHONE   |   khelp.samples.databaase.OS:IOS   |  IPhone9   |    9    |
| 3  | MyComputer | khelp.samples.databaase.DeviceType:COMPUTER |  khelp.samples.databaase.OS:LINUX  |   Ubuntu   |   11    |
| 4  |    Work    | khelp.samples.databaase.DeviceType:DESKTOP  |  khelp.samples.databaase.OS:LINUX  |   Ubuntu   |   11    |
+----+------------+---------------------------------------------+------------------------------------+------------+---------+

2020-12-18 07:33:11:291 INFORMATION khelp.samples.databaase.SelectPrintKt.main at 79: *=> MARK
************************
***   Model and OS   ***
************************
+-------------------------------------------------+
|                     Device                      |
+------------+------------------------------------+
|   Model    |                 OS                 |
+------------+------------------------------------+
| HammerHead | khelp.samples.databaase.OS:ANDROID |
|  IPhone9   |   khelp.samples.databaase.OS:IOS   |
|   Ubuntu   |  khelp.samples.databaase.OS:LINUX  |
|   Ubuntu   |  khelp.samples.databaase.OS:LINUX  |
+------------+------------------------------------+

2020-12-18 07:33:11:296 INFORMATION khelp.samples.databaase.SelectPrintKt.main at 87: *=> MARK
*********************************
***   Model and OS filtered   ***
*********************************
+-------------------------------------------------+
|                     Device                      |
+------------+------------------------------------+
|   Model    |                 OS                 |
+------------+------------------------------------+
| HammerHead | khelp.samples.databaase.OS:ANDROID |
|   Ubuntu   |  khelp.samples.databaase.OS:LINUX  |
|   Ubuntu   |  khelp.samples.databaase.OS:LINUX  |
+------------+------------------------------------+
2020-12-18 07:33:11:305 INFORMATION khelp.samples.databaase.SelectPrintKt.main at 95: *=> MARK
******************************************
***   All table order name ascendant   ***
******************************************
+---------------------------------------------------------------------------------------------------------------------------+
|                                                          Device                                                           |
+----+------------+---------------------------------------------+------------------------------------+------------+---------+
| ID |    Name    |                 Device_Type                 |                 OS                 |   Model    | Version |
+----+------------+---------------------------------------------+------------------------------------+------------+---------+
| 2  |  IPhone9   |  khelp.samples.databaase.DeviceType:PHONE   |   khelp.samples.databaase.OS:IOS   |  IPhone9   |    9    |
| 3  | MyComputer | khelp.samples.databaase.DeviceType:COMPUTER |  khelp.samples.databaase.OS:LINUX  |   Ubuntu   |   11    |
| 1  |  Pixel 3   |  khelp.samples.databaase.DeviceType:PHONE   | khelp.samples.databaase.OS:ANDROID | HammerHead |   11    |
| 4  |    Work    | khelp.samples.databaase.DeviceType:DESKTOP  |  khelp.samples.databaase.OS:LINUX  |   Ubuntu   |   11    |
+----+------------+---------------------------------------------+------------------------------------+------------+---------+

2020-12-18 07:33:11:309 INFORMATION khelp.samples.databaase.SelectPrintKt.main at 102: *=> MARK
*****************************************
***   Model and OS descendant model   ***
*****************************************
+-------------------------------------------------+
|                     Device                      |
+------------+------------------------------------+
|   Model    |                 OS                 |
+------------+------------------------------------+
|   Ubuntu   |  khelp.samples.databaase.OS:LINUX  |
|   Ubuntu   |  khelp.samples.databaase.OS:LINUX  |
|  IPhone9   |   khelp.samples.databaase.OS:IOS   |
| HammerHead | khelp.samples.databaase.OS:ANDROID |
+------------+------------------------------------+
```

Notice that we use `printDataRowResult` to print the result. This method is useful for debugging, to check if request returns what expected.
This method consumes the result, like we will see in next part result can be read only one time from first result to last.

We don't recommend to print result in log for production, else it is useless to crypt database if print it's content in logs.

The first request 
```kotlin
var result = tableDevice.select { }
``` 
is the minimal one.
We don't select columns, so all table columns are selected.
We don't do any filter, so all table content will be shown.
We don't specify any order, so rows will appear in insertion order.

The second request 
```kotlin
    result = tableDevice.select {
        +COLUMN_MODEL
        +COLUMN_OS
    }
```
Shows how select columns in result. Only selected columns will appear in result

The third request
```kotlin
    result = tableDevice.select {
        +COLUMN_MODEL
        +COLUMN_OS
        where { condition = COLUMN_VERSION EQUALS 11 }
    }
```
Shows how specify a condition to filter the result. 
For more information about conditions see [Where and conditions](WhereAndConditions.md)

The fourth request
```kotlin
    result = tableDevice.select {
        ascendant(COLUMN_NAME)
    }
```
Shows how sort by a column in ascendant order. 
Just specify the column to sort  in `ascendant` method


The last request
```kotlin
    result = tableDevice.select {
        +COLUMN_MODEL
        +COLUMN_OS
        descendant(COLUMN_MODEL)
    }
```
Shows how sort by a column in descendant order.
Just specify the column to sort in `descendant` method

Only one column can be sorted.

## Exploit the result

`select` return a `khelp.database.DataRowResult`

This result read from first row result to last one. In this way and only one time.

```kotlin
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
```

Prints
```shell
************************
***   Model and OS   ***
************************
> ANDROID : HammerHead
> IOS : IPhone9
> LINUX : Ubuntu
> LINUX : Ubuntu
```

In `next` lambda get column value. The number is the range of the column is selection. First selected column is **1**, second **2**, and so on.

To get the data, use the appropriate `get...` method depends on columns data type. 
More information about type in [Data types](ColumnsAndTable.md#data-types)

When result is finished to be exploited, use `close` method to close properly all database link.

[Menu](Menu.md)