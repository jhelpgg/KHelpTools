package khelp.samples.databaase

import khelp.database.Database
import khelp.database.databaseobject.DatabaseObject
import khelp.database.databaseobject.PrimaryKey

class AddressBook(@PrimaryKey val name: String, val addresses: Array<Address>, database: Database) :
    DatabaseObject(database)
{
}