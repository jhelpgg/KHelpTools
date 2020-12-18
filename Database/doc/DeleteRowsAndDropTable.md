# Delete rows and table

1. [Delete some rows](#delete-some-rows)
1. [Delete a table](#delete-a-table)

Here we explain how delete things

## Delete some rows

To delete some rows, use `delete` method of `Table`

```kotlin
    tableDevice.delete { where { condition = COLUMN_VERSION EQUALS 9 } }
```

`where` specifies condition on rows to be deleted. 
If not specify, all rows will be deleted.
For more on conditions : [Where and conditions](WhereAndConditions.md)

## Delete a table

```kotlin
    tableDevice.delete {  }
```

will make the table empty, but still their.

To remove completely the table (all data in it will be lost):

```kotlin
    database.dropTable(tableDevice)
```

[Menu](Menu.md)
