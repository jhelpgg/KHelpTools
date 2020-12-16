package khelp.database.exception

import khelp.database.type.DataType

/**
 * Throws if data not have expected type
 */
class InvalidDataTypeException(invalidType: DataType, expectedType: DataType) :
    Exception("This method require data type $expectedType but used with $invalidType")

