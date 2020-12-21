package khelp.database.databaseobject

import khelp.database.Database
import khelp.utilities.extensions.bounds

class TestPerson(@PrimaryKey val name: String, age: Int, address: TestAddress, database: Database) :
    DatabaseObject(database)
{
    var age: Int = age.bounds(0, 123)
        set(value)
        {
            field = value.bounds(0, 123)
            this.update()
        }

    var address: TestAddress = address
        set(value)
        {
            field = value
            this.update()
        }
}