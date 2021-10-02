package khelp.io.files.json

class ArrayJson
{
    private val content = ArrayList<Pair<JsonDataType, Any>>()
    val size : Int get() = this.content.size

    fun addNull()
    {
        this.content.add(Pair(JsonDataType.NULL, ""))
    }

    fun addBoolean(value:Boolean)
    {
        this.content.add(Pair(JsonDataType.BOOLEAN, value))
    }

    fun addNumber(value:Number)
    {
        this.content.add(Pair(JsonDataType.NUMBER, value.toDouble()))
    }

    fun addString(value:String)
    {
        this.content.add(Pair(JsonDataType.STRING, value))
    }

    fun addObject(value:ObjectJson)
    {
        this.content.add(Pair(JsonDataType.OBJECT, value))
    }

    fun addArray(value:ArrayJson)
    {
        this.content.add(Pair(JsonDataType.ARRAY, value))
    }

    fun type(index:Int) : JsonDataType = this.content[index].first

    fun isNull(index : Int) : Boolean = this.content[index].first == JsonDataType.NULL

    @Throws(JsonException::class)
    fun boolean(index:Int) : Boolean
    {
        val (type,value) = this.content[index]

        if (type != JsonDataType.BOOLEAN)
        {
            throw JsonException("Value at index $index is not Boolean but $type")
        }

        return value as Boolean
    }

    @Throws(JsonException::class)
    fun double(index:Int) : Double
    {
        val (type,value) = this.content[index]

        if (type != JsonDataType.NUMBER)
        {
            throw JsonException("Value at index $index is not Number but $type")
        }

        return value as Double
    }

    @Throws(JsonException::class)
    fun float(index:Int) : Float =        this.double(index).toFloat()

    @Throws(JsonException::class)
    fun long(index:Int) : Long =        this.double(index).toLong()

    @Throws(JsonException::class)
    fun int(index:Int) : Int =        this.double(index).toInt()

    @Throws(JsonException::class)
    fun short(index:Int) : Short =        this.int(index).toShort()

    @Throws(JsonException::class)
    fun byte(index:Int) : Byte =        this.int(index).toByte()


    @Throws(JsonException::class)
    fun string(index:Int) : String
    {
        val (type,value) = this.content[index]

        if (type != JsonDataType.STRING)
        {
            throw JsonException("Value at index $index is not String but $type")
        }

        return value as String
    }


    @Throws(JsonException::class)
    fun objectJson(index:Int) : ObjectJson
    {
        val (type,value) = this.content[index]

        if (type != JsonDataType.OBJECT)
        {
            throw JsonException("Value at index $index is not Object but $type")
        }

        return value as ObjectJson
    }

    @Throws(JsonException::class)
    fun arrayJson(index:Int) : ArrayJson
    {
        val (type,value) = this.content[index]

        if (type != JsonDataType.ARRAY)
        {
            throw JsonException("Value at index $index is not Array but $type")
        }

        return value as ArrayJson
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
            jsonWriter.startArray()
        }
        else
        {
            jsonWriter.startArray(name)
        }

        for((type,value) in this.content)
        {
            when (type)
            {
                JsonDataType.NULL    -> jsonWriter.putNull()
                JsonDataType.BOOLEAN -> jsonWriter.putBoolean( value as Boolean)
                JsonDataType.NUMBER  -> jsonWriter.putNumber( value as Double)
                JsonDataType.STRING  -> jsonWriter.putString( value as String)
                JsonDataType.OBJECT  -> (value as ObjectJson).serialize(jsonWriter,"")
                JsonDataType.ARRAY   -> (value as ArrayJson).serialize(jsonWriter, "")
            }
        }

        jsonWriter.endArray()
    }
}