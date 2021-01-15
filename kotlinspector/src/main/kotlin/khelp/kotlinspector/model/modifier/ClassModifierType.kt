package khelp.kotlinspector.model.modifier

enum class ClassModifierType(val modifier: String)
{
    ENUM("enum"),
    SEALED("sealed"),
    ANNOTATION("annotation"),
    DATA("data"),
    INNER("inner"),
    NONE("");

    companion object
    {
        fun parse(modifier: String): ClassModifierType
        {
            val trimmed = modifier.trim()

            for (classModifierType in ClassModifierType.values())
            {
                if (trimmed == classModifierType.modifier)
                {
                    return classModifierType
                }
            }

            return ClassModifierType.NONE
        }
    }
}