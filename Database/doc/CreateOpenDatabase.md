# Create and open database

The first thing is to create the database or open it if it exists. 
Have to specify database relative path. Relative path separator is **/**

To create or open the database:

```kotlin
package khelp.samples.databaase

import khelp.database.Database

fun main()
{
    // Create or open the database
    val database = Database.database("login", "password", "data/createOpen/database")

    // TODO Manipulate the database

    // Always close properly the database before exit
    database.close()
}
```

Close the database we need no more use it. The close will commit last modifications to be sure they are take care and
will close the database properly. This permits to **HSQL** to create its final files and guarantee the next open safe.

If the database is already open, the method will check the login and password and returned the database instance.

If the database is closed by `close` method, reopen the database is possible but give another instance of database object.

[Menu](Menu.md)
