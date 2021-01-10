package khelp.kotlinspector.model.modifier

enum class PlatformModifierType(val modifier:String)
{
    EXPECT("expect"),
    ACTUAL("actual"),
    NONE("");

    companion object
    {
        fun parse(modifier: String): PlatformModifierType
        {
            for (inheritanceModifierType in PlatformModifierType.values())
            {
                if (inheritanceModifierType.modifier == modifier)
                {
                    return inheritanceModifierType
                }
            }

            return PlatformModifierType.NONE
        }
    }
}