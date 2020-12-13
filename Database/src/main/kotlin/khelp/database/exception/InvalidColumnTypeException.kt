package khelp.database.exception

import khelp.database.type.DataType

class InvalidColumnTypeException(invalidType:DataType, expectedType:DataType) : Exception("This method require data type $expectedType but used with $invalidType")

