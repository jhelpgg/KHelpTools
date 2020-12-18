# Where and conditions

1. [Defines a condition](#defines-a-condition)
1. [Conditions list](#conditions-list)

In databases instructions : Update, Delete or Select its possible to filter rows that implies in the instruction.

This filter is a condition that one or more table columns in a row must match to be selected.

Here we describe how specifies those condition and presents all possible conditions.

Just remember to use the good type for a column, only **ID** and **INTEGER** can be compatible, other are exclusive.
See [Data types](ColumnsAndTable.md#data-types) for more details about data types

## Defines a condition

Each operation has a DSL instruction for specify the filter condition. For Update, Delete or Select it is calle `where`
and fo insert or update `updateIfExactlyOneRowMatch`

We will give syntax examples with `where`, but remember the content is the same inside `updateIfExactlyOneRowMatch`

to specify a condition, just defines the `condition` field, by example:

```kotlin
where { condition = "Name" EQUALS name }
```

Here it filters rows from the table where column **Name** is equals to `name`

## Conditions list

Conditions link to a column can be referenced by a `Column` instance or their name

`EQUALS_ID` is to indicate the row with given id.

```kotlin
where { COLUMN_ID EQUALS_ID id }
```

`EQUALS` is uses for filter columns with a specific value

```kotlin
where { "Name" EQUALS "John Doe" }
```

```kotlin
where { "Bank_Account" EQUALS 123.45 }
```

`NOT_EQUALS_ID` filters all rows not match to given id

```kotlin
where { NOT_EQUALS_ID EQUALS_ID id }
```

`NOT_EQUALS` is uses for filter columns different from specific value

```kotlin
where { "Name" NOT_EQUALS "John Doe" }
```

```kotlin
where { "Bank_Account" NOT_EQUALS 123.45 }
```

All followed conditions respect this logic

```kotlin
where { <Colmn or column name ><Condtion><Value> }
```

| Condition | Description |
|:-----:|-----|
| `EQUALS_ID` | Choose row that have the given id |
| `EQUALS` | Choose row with specified column have given value |
| `NOT_EQUALS_ID` | Choose rows that have not the given id |
| `NOT_EQUALS` | Choose row with specified column without given value |
| `LOWER` | Choose row with specified column have value strictly lower to given one |
| `LOWER_EQUALS` | Choose row with specified column have value lower or equals to given one |
| `UPPER` | Choose row with specified column have value strictly upper to given one |
| `UPPER_EQUALS` | Choose row with specified column have value upper or equals to given one |
| `REGEX` Choose row with specified column of String type have value match given regular expression |

To select row where columns are one of elements inside an array of possibilities, there `ONE_OF_ID`(for ID), `ONE_OF` (
for others)

```kotlin
where { condition = COLUMN_ID ONE_OF_ID intArrayOf(22, 33, 42, 73) }
```

```kotlin
where { condition = "Name" ONE_OF arrayOf("John Doe", "Arthur", "Skwwek") }
```

The column can match one of possible result of selection inside another table. The selection must select one and only
one column. The selected column and the column we filter must have compatible type.

More about selection : [Select rows and print result](SelectRowsAndPrintResult.md)

More about data type : [Data types](ColumnsAndTable.md#data-types)

```kotlin
where {
    condition = METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TABLE_ID IN {
        select(metadataTableOfTables) {
            +COLUMN_ID
            where { condition = METADATA_TABLE_OF_TABLES_COLUMN_TABLE EQUALS tableName }
        }
    }
}
```

Condition can be reversed

```kotlin
where { condition = not("Name" REGEX pattern) }
```

To have two conditions must match in same time can use `AND`

```kotlin
where { condition = ("Name" REGEX pattern) AND ("Age" UPPER_EQUALS 18) }
```

To have at least one condition match can use `OR`

```kotlin
where { condition = ("Amount" LOWER 123.0) OR ("Amount" UPPER 1234.0) }
```

Since `not`, `AND` and `OR` are condition, they can be combine in complexe way

[Menu](Menu.md)
