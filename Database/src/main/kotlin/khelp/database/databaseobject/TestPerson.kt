package khelp.database.databaseobject

import khelp.database.Database
import khelp.utilities.extensions.bounds

class TestPerson(@PrimaryKey val name: String, age: Int, var address: TestAddress, database: Database) :
    DatabaseObject(database)
{
    var age: Int = age.bounds(0, 123)
        private set

    fun setAge(age: Int)
    {
        this.age = age.bounds(0, 123)
        this.update()
    }
}