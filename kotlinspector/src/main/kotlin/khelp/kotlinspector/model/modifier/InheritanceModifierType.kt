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
            for (inheritanceModifierType in InheritanceModifierType.values())
            {
                if (inheritanceModifierType.modifier == modifier)
                {
                    return inheritanceModifierType
                }
            }

            return InheritanceModifierType.NONE
        }
    }
}