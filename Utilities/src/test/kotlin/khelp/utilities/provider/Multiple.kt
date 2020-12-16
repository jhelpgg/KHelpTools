package khelp.utilities.provider

private val instances = arrayOf(AnswerTest, MagicTest)
private var index = 0

fun cycle(): TestInterface
{
    val response = instances[index]
    index = (index + 1) % instances.size
    return response
}
