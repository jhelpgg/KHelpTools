package khelp.kotlinspector.model.modifier

enum class VisibilityModifierType(val modifier:String)
{
    PUBLIC("public"),
    PRIVATE("private"),
    INTERNAL("internal"),
    PROTECTED("protected"),
    NONE("");

    companion object
    {
        fun parse(modifier: String): VisibilityModifierType
        {
            for (inheritanceModifierType in VisibilityModifierType.values())
            {
                if (inheritanceModifierType.modifier == modifier)
                {
                    return inheritanceModifierType
                }
            }

            return VisibilityModifierType.NONE
        }
    }
}