package khelp.preferences

import khelp.utilities.serialization.ParsableSerializable
import khelp.utilities.serialization.Parser
import khelp.utilities.serialization.Serializer

class Address(street:String="") : ParsableSerializable
{
    var street:String = street
    private set

    override fun serialize(serializer : Serializer)
    {
        serializer.setString("street", this.street)
    }

    override fun parse(parser : Parser)
    {
        this.street = parser.getString("street")
    }
}