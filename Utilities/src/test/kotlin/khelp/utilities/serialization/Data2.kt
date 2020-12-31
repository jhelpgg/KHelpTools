package khelp.utilities.serialization

class Data2(var doubles: DoubleArray, var data1: Data1) : ParsableSerializable
{
    val map = HashMap<String, String>()
    val others = ArrayList<Data1>()

    override fun serialize(serializer: Serializer)
    {
        serializer.setDoubleArray("doubles", this.doubles)
        serializer.setParsableSerializable("data1", this.data1)
        serializer.setStringMap("map", this.map)
        serializer.setParsableSerializableList("others", this.others)
    }

    override fun parse(parser: Parser)
    {
        this.doubles = parser.getDoubleArray("doubles")
        this.data1 = parser.getParsableSerializable("data1") { this.data1 }
        this.map.clear()
        parser.putStringMap("map", this.map)
        this.others.clear()
        parser.appendParsableSerializableList("others", this.others) { Data1(false, ' ', ByteArray(0)) }
    }
}