package khelp.kotlinspector.model

enum class ThisOrSuper
{
    THIS,
    SUPER,
    NONE;

    companion object
    {
        fun parse(serialized: String): ThisOrSuper =
            when (serialized.trim())
            {
                "this"  -> ThisOrSuper.THIS
                "super" -> ThisOrSuper.SUPER
                else    -> ThisOrSuper.NONE
            }
    }
}