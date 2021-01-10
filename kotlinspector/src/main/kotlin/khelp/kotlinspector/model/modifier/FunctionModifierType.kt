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
            for (functionModifierType in FunctionModifierType.values())
            {
                if (functionModifierType.modifier == modifier)
                {
                    return functionModifierType
                }
            }

            return FunctionModifierType.NONE
        }
    }
}