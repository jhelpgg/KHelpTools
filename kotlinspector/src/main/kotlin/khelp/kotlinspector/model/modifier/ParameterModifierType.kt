package khelp.kotlinspector.model.modifier

enum class ParameterModifierType(val modifier:String)
{
    VARARG("vararg"),
    NOINLINE("noinline"),
    CROSSINLINE("crossinline"),
    NONE("");

    companion object
    {
        fun parse(modifier: String): ParameterModifierType
        {
            val trimmed = modifier.trim()

            for (inheritanceModifierType in ParameterModifierType.values())
            {
                if (inheritanceModifierType.modifier == trimmed)
                {
                    return inheritanceModifierType
                }
            }

            return ParameterModifierType.NONE
        }
    }
}