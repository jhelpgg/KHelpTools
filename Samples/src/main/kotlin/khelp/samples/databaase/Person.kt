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