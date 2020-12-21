package khelp.database.databaseobject

import khelp.database.Database

class TestAddress(@PrimaryKey val street: String, @PrimaryKey val number: Int, database: Database) : DatabaseObject(database)
{
}