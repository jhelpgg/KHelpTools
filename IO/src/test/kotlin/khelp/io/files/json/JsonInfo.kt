package khelp.io.files.json

sealed class JsonInfo

class ErrorParsing(val jsonReaderException : JsonReaderException) : JsonInfo()

object StartDocument : JsonInfo()
object EndDocument : JsonInfo()

class StartObject(val name : String = "") : JsonInfo()
object EndObject : JsonInfo()

class StartArray(val name : String = "") : JsonInfo()
object EndArray : JsonInfo()

class MeetNull(val name : String = "") : JsonInfo()
class MeetBoolean(val value : Boolean, val name : String = "") : JsonInfo()
class MeetNumber(val value : Double, val name : String = "") : JsonInfo()
class MeetString(val value : String, val name : String = "") : JsonInfo()