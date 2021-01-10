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
            for (classModifierType in MemberModifierType.values())
            {
                if (modifier == classModifierType.modifier)
                {
                    return classModifierType
                }
            }

            return MemberModifierType.NONE
        }
    }
}