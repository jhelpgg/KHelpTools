package khelp.database.exception

import khelp.database.Column

class NoValueDefinedAndNoDefaultValue(column: Column) : Exception("No value defined for ${column.name} and no default value exists for ${column.type}")
