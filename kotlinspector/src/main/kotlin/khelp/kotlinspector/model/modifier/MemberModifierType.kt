package khelp.kotlinspector.model.modifier

enum class MemberModifierType(val modifier: String)
{
    OVERRIDE("override"),
    LATEINIT("lateinit"),
    NONE("");

    companion object
    {
        fun parse(modifier: String): MemberModifierType
        {
            val trimmed = modifier.trim()

            for (classModifierType in MemberModifierType.values())
            {
                if (trimmed == classModifierType.modifier)
                {
                    return classModifierType
                }
            }

            return MemberModifierType.NONE
        }
    }
}