package khelp.preferences

import khelp.utilities.serialization.ParsableSerializable
import khelp.utilities.serialization.Parser
import khelp.utilities.serialization.Serializer

class Person(name : String="", address : Address=Address()) : ParsableSerializable,
                                                 Iterable<Person>
{
    var name : String = name
        private set
    var address : Address = address
        private set
    private val children = ArrayList<Person>()

    override fun serialize(serializer : Serializer)
    {
        serializer.setString("name", this.name)
        serializer.setParsableSerializable("address", this.address)
        serializer.setParsableSerializableList("children", this.children)
    }

    override fun parse(parser : Parser)
    {
        this.name = parser.getString("name")
        this.address = parser.getParsableSerializable("address") { Address() }
        this.children.clear()
        parser.appendParsableSerializableList("children", this.children) { Person() }
    }

    operator fun plusAssign(child : Person)
    {
        this.children.add(child)
    }

    override fun iterator() : Iterator<Person> = this.children.iterator()
}