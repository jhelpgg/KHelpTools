package khelp.utilities.serialization

/**
 * Add capacity to an object to be serialized and parsed
 *
 * Serializer/Parser instance depends on target/source format
 */
interface ParsableSerializable
{
    fun serialize(serializer: Serializer)

    fun parse(parser: Parser)
}