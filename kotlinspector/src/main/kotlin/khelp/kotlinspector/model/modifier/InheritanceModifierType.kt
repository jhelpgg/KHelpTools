package khelp.kotlinspector.model.modifier

enum class InheritanceModifierType(val modifier:String)
{
    ABSTRACT("abstract"),
    FINAL("final"),
    OPEN("open"),
    NONE("");

    companion object
    {
        fun parse(modifier: String): InheritanceModifierType
        {
            val trimmed = modifier.trim()

            for (inheritanceModifierType in InheritanceModifierType.values())
            {
                if (inheritanceModifierType.modifier == trimmed)
                {
                    return inheritanceModifierType
                }
            }

            return InheritanceModifierType.NONE
        }
    }
}