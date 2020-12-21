package khelp.samples.databaase

import khelp.database.Database
import khelp.database.databaseobject.DatabaseObject
import khelp.database.databaseobject.PrimaryKey

class Address(@PrimaryKey val street: String, @PrimaryKey val number: Int, database: Database) : DatabaseObject(database)
{
}