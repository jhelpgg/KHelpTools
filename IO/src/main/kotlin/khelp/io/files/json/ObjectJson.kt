package khelp.io.files.json

class ObjectJson
{
    private val content = HashMap<String, Pair<JsonDataType, Any>>()
    val size : Int get() = this.content.size

    @Throws(JsonException::class)
    fun putNull(name : String)
    {
        val key = name.trim()

        if (key.isEmpty())
        {
            throw JsonException("Name must not be empty or full of white characters")
        }

        this.content[key] = Pair(JsonDataType.NULL, "")
    }

    @Throws(JsonException::class)
    fun putBoolean(name : String, value : Boolean)
    {
        val key = name.trim()

        if (key.isEmpty())
        {
            throw JsonException("Name must not be empty or full of white characters")
        }

        this.content[key] = Pair(JsonDataType.BOOLEAN, value)
    }

    @Throws(JsonException::class)
    fun putNumber(name : String, value : Number)
    {
        val key = name.trim()

        if (key.isEmpty())
        {
            throw JsonException("Name must not be empty or full of white characters")
        }

        this.content[key] = Pair(JsonDataType.NUMBER, value.toDouble())
    }


    @Throws(JsonException::class)
    fun putString(name : String, value : String)
    {
        val key = name.trim()

        if (key.isEmpty())
        {
            throw JsonException("Name must not be empty or full of white characters")
        }

        this.content[key] = Pair(JsonDataType.STRING, value)
    }


    @Throws(JsonException::class)
    fun putObject(name : String, value : ObjectJson)
    {
        val key = name.trim()

        if (key.isEmpty())
        {
            throw JsonException("Name must not be empty or full of white characters")
        }

        this.content[key] = Pair(JsonDataType.OBJECT, value)
    }

    @Throws(JsonException::class)
    fun putArray(name : String, value : ArrayJson)
    {
        val key = name.trim()

        if (key.isEmpty())
        {
            throw JsonException("Name must not be empty or full of white characters")
        }

        this.content[key] = Pair(JsonDataType.ARRAY, value)
    }

    operator fun contains(key : String) : Boolean = key in this.content

    fun keys() : Iterable<String> = this.content.keys

    @Throws(JsonException::class)
    fun type(key : String) : JsonDataType =
        this.content[key]?.first ?: throw JsonException("Key $key is not defined")

    @Throws(JsonException::class)
    fun isNull(key : String) : Boolean
    {
        val value = this.content[key] ?: throw JsonException("Key $key is not defined")
        return value.first == JsonDataType.NULL
    }

    @Throws(JsonException::class)
    fun boolean(key : String) : Boolean
    {
        val value = this.content[key] ?: throw JsonException("Key $key is not defined")

        if (value.first != JsonDataType.BOOLEAN)
        {
            throw JsonException("Key $key is not Boolean but ${value.first}")
        }

        return value.second as Boolean
    }

    @Throws(JsonException::class)
    fun double(key : String) : Double
    {
        val value = this.content[key] ?: throw JsonException("Key $key is not defined")

        if (value.first != JsonDataType.NUMBER)
        {
            throw JsonException("Key $key is not Number but ${value.first}")
        }

        return value.second as Double
    }

    @Throws(JsonException::class)
    fun float(key : String) : Float = this.double(key)
        .toFloat()

    @Throws(JsonException::class)
    fun long(key : String) : Long = this.double(key)
        .toLong()

    @Throws(JsonException::class)
    fun int(key : String) : Int = this.double(key)
        .toInt()

    @Throws(JsonException::class)
    fun short(key : String) : Short = this.int(key)
        .toShort()

    @Throws(JsonException::class)
    fun byte(key : String) : Byte = this.int(key)
        .toByte()

    @Throws(JsonException::class)
    fun string(key : String) : String
    {
        val value = this.content[key] ?: throw JsonException("Key $key is not defined")

        if (value.first != JsonDataType.STRING)
        {
            throw JsonException("Key $key is not String but ${value.first}")
        }

        return value.second as String
    }

    @Throws(JsonException::class)
    fun objectJson(key : String) : ObjectJson
    {
        val value = this.content[key] ?: throw JsonException("Key $key is not defined")

        if (value.first != JsonDataType.OBJECT)
        {
            throw JsonException("Key $key is not Object but ${value.first}")
        }

        return value.second as ObjectJson
    }

    @Throws(JsonException::class)
    fun arrayJson(key : String) : ArrayJson
    {
        val value = this.content[key] ?: throw JsonException("Key $key is not defined")

        if (value.first != JsonDataType.ARRAY)
        {
            throw JsonException("Key $key is not Array but ${value.first}")
        }

        return value.second as ArrayJson
    }

    fun clear()
    {
        this.content.clear()
    }

    @Throws(JsonWriterException::class)
    fun serialize(jsonWriter : JsonWriter)
    {
        this.serialize(jsonWriter, "")
        jsonWriter.finish()
    }

    @Throws(JsonWriterException::class)
    internal fun serialize(jsonWriter : JsonWriter, name : String)
    {
        if (name.isEmpty())
        {
            jsonWriter.startObject()
        }
        else
        {
            jsonWriter.startObject(name)
        }

        for ((key, pair) in this.content)
        {
            val (type, value) = pair

            when (type)
            {
                JsonDataType.NULL    -> jsonWriter.putNull(key)
                JsonDataType.BOOLEAN -> jsonWriter.putBoolean(key, value as Boolean)
                JsonDataType.NUMBER  -> jsonWriter.putNumber(key, value as Double)
                JsonDataType.STRING  -> jsonWriter.putString(key, value as String)
                JsonDataType.OBJECT  -> (value as ObjectJson).serialize(jsonWriter, key)
                JsonDataType.ARRAY   -> (value as ArrayJson).serialize(jsonWriter, key)
            }
        }

        jsonWriter.endObject()
    }
}