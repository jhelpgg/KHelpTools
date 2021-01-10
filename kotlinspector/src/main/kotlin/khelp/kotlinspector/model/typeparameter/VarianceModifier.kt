package khelp.kotlinspector.model.typeparameter

enum class VarianceModifier
{
    NONE,
    IN,
    OUT;

    companion object
    {
        fun parse(serialized: String): VarianceModifier =
            when (serialized)
            {
                "in"  -> VarianceModifier.IN
                "out" -> VarianceModifier.OUT
                else  -> VarianceModifier.NONE
            }
    }
}