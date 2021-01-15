package khelp.kotlinspector.model.modifier

enum class FunctionModifierType(val modifier: String)
{
    TAILREC("tailrec"),
    OPERATOR("operator"),
    INFIX("infix"),
    INLINE("inline"),
    EXTERNAL("external"),
    SUSPEND("suspend"),
    NONE("");

    companion object
    {
        fun parse(modifier: String): FunctionModifierType
        {
            val trimmed = modifier.trim()

            for (functionModifierType in FunctionModifierType.values())
            {
                if (functionModifierType.modifier == trimmed)
                {
                    return functionModifierType
                }
            }

            return FunctionModifierType.NONE
        }
    }
}