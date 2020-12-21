# Database objects

The idea behind database objects is to add an abstract layer over the database to simplify database requests.

Manipulate Table and columns manipulations still possible, but not necessary.

Database object represents an object. 
Create a database object, creates automatically an associated table, where each column will be a object field.
Field available type are same as database type (see [Data types](ColumnsAndTable.md#data-types)) or any other database object.

It is developer responsibility to protected database object from any renaming or shrinkings tools as proguard. 
The system supposes that class and fields names are still the same. 

A database object, extends the abstract class `khelp.database.databaseobject.DatabaseObject`

For example:

```kotlin
package khelp.samples.databaase

import khelp.database.Database
import khelp.database.databaseobject.DatabaseObject
import khelp.database.databaseobject.PrimaryKey

class Address(@PrimaryKey val street: String, @PrimaryKey val number: Int, database: Database) : DatabaseObject(database)
{
}
```

```kotlin
package khelp.samples.databaase

import khelp.database.Database
import khelp.database.databaseobject.DatabaseObject
import khelp.database.databaseobject.PrimaryKey
import khelp.utilities.extensions.bounds

class Person(@PrimaryKey val name: String, age: Int, address: Address, database: Database) :
    DatabaseObject(database)
{
    var age: Int = age.bounds(0, 123)
        set(value)
        {
            field = value.bounds(0, 123)
            this.update()
        }

    var address: Address = address
        set(value)
        {
            field = value
            this.update()
        }
}
```

Indicates some fields as `PrimaryKey`, say the fields necessary for distinguish two value.
In our examples, two addresses are same if `street` and `number`have same values.
Two persons are considered the same if have same `name`.

`PrimaryKey` annotation can only apply to final types. 

The database is the database where the instance will be stored/update.

In `Person` example, we see a call to `update` method on each change.
Call this protected method, stores new values in the database.

Here `Address` is linked to `Person`. It is not recommended linking it inside another object.
Avoid circular reference, it will lead to infinite loop.

To create an instance :

```kotlin
    Person("Arthur",
           42,
           Address("Jump Street", 21, database).waitCreated(),
           database).waitCreated<Person>()

```

This will automatically add the person and the address to database.

Since register inside the database takes a little time, it is recommended the instance is relly really ready before use it.
It is the role of `waitCreated`

To select a database objects that meet a criteria. For example, all person between **40** and **50** years old:

```kotlin
    val result = DatabaseObject.select<Person>(database) {
        condition = (Person::age UPPER_EQUALS 40) AND (Person::age LOWER 50)
    }
```

By selecting by the object property it avoids typing issues.
The possible columns are clear.
Automatic type checking. Here it will not compile if specify a **String** for `age`

to read the result :
```kotlin
    while (result.hasNext)
    {
        val person = result.next()
        debug(">> ${person.name} : ${person.age} : ${person.address.street} : ${person.address.number}")
    }
    result.close()
```

Don't forget to close the result a*when no more need it.

The `condition =` is mandatory. for a list of possibilities, check [Conditions list](WhereAndConditions.md#conditions-list).
`EQUALS`, `NOT_EQUALS` and `ONE_OF`are extends to database object.

As we said, insert is done by just create an instance and update by the protected method `update`

We recommend to use same trick as `Person` to udpate as soon as a value change.

For delete database object tha match elements:

```kotlin
    DatabaseObject.delete<Person>(database) {
        condition = Person::age EQUALS 42
    }
```

Condition follow the same rules as previously.

It is possible to delete a specific instance by :

```kotlin
DatabaseObject.delete(person)
```

Don't delete link object (here `Address`) manually if want keeps coherence. The system will clear automatically no more used ones.

To get a database object table :
```kotlin
DatabaseObject.table(database, Person::class.java)
```

For exemple print `Person` and `Address` content
```kotlin
    printDataRowResult(DatabaseObject.table(database, Person::class.java)
                           .select { }, System.out)

    printDataRowResult(DatabaseObject.table(database, Address::class.java)
                           .select { }, System.out)
```

A more complete exemple
```kotlin
package khelp.samples.databaase

import khelp.database.Database
import khelp.database.condition.AND
import khelp.database.databaseobject.DatabaseObject
import khelp.database.printDataRowResult
import khelp.utilities.log.debug
import khelp.utilities.log.mark

fun main()
{
    val database = Database.database("login", "password", "data/database_object/database")
    mark("Before")
    printDataRowResult(DatabaseObject.table(database, Address::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Person::class.java)
                           .select { }, System.out)

    Person("Hello",
           42,
           Address("Street", 21, database).waitCreated(),
           database).waitCreated<Person>()
    Person("Arthur",
           42,
           Address("Street", 21, database).waitCreated(),
           database).waitCreated<Person>()
    val joe = Person("Joe",
                     73,
                     Address("Street", 21, database).waitCreated(),
                     database).waitCreated<Person>()
    val dandy = Person("Space Dandy",
                       21,
                       Address("Space", 7777777, database).waitCreated(),
                       database).waitCreated<Person>()
    Person("John",
           45,
           Address("Street", 21, database).waitCreated(),
           database).waitCreated<Person>()
    printDataRowResult(database.metadataTableOfTables.select { }, System.out)
    printDataRowResult(database.metadataTableOfTablesColumn.select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Person::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Address::class.java)
                           .select { }, System.out)

    val result = DatabaseObject.select<Person>(database) {
        condition = (Person::age UPPER 40) AND (Person::age LOWER 50)
    }
    while (result.hasNext)
    {
        val p = result.next()
        debug(">> ${p.name} : ${p.age} : ${p.address.street} : ${p.address.number}")
    }
    result.close()

    mark("REMOVE")
    val nb = DatabaseObject.delete<Person>(database) {
        condition = Person::age EQUALS 42
    }

    mark("nb=$nb")

    val done = DatabaseObject.delete(dandy)
    mark("done=$done")

    printDataRowResult(DatabaseObject.table(database, Person::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Address::class.java)
                           .select { }, System.out)

    mark("Change Joe")
    joe.age = 37
    joe.address = Address("Somewhere", 12, database).waitCreated()
    printDataRowResult(DatabaseObject.table(database, Person::class.java)
                           .select { }, System.out)
    printDataRowResult(DatabaseObject.table(database, Address::class.java)
                           .select { }, System.out)


    database.close()
}
```
