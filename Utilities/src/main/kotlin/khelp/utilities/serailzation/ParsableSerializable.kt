package khelp.utilities.serailzation

interface ParsableSerializable
{
    fun serialize(serializer: Serializer)

    fun parse(parser: Parser)
}