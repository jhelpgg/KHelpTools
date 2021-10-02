package khelp.io.files.json

class JsonReaderException(message : String, val lineNumber : Int, val characterIndex : Int,
                          cause : Throwable? = null) : JsonException(message, cause)
