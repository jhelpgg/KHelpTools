package khelp.kotlinspector.model.modifier

enum class PlatformModifierType(val modifier: String)
{
    EXPECT("expect"),
    ACTUAL("actual"),
    NONE("");

    companion object
    {
        fun parse(modifier: String): PlatformModifierType
        {
            val trimmed = modifier.trim()

            for (inheritanceModifierType in PlatformModifierType.values())
            {
                if (inheritanceModifierType.modifier == trimmed)
                {
                    return inheritanceModifierType
                }
            }

            return PlatformModifierType.NONE
        }
    }
}