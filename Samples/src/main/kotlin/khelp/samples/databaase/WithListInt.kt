package khelp.samples.databaase

import khelp.database.Database
import khelp.database.databaseobject.DatabaseObject
import khelp.database.databaseobject.PrimaryKey

class WithListInt(@PrimaryKey val numberKey:Int, val arrayOfInt:IntArray, database: Database) : DatabaseObject(database)
{
}