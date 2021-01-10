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
            for (classModifierType in ClassModifierType.values())
            {
                if (modifier == classModifierType.modifier)
                {
                    return classModifierType
                }
            }

            return ClassModifierType.NONE
        }
    }
}