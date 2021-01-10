package khelp.kotlinspector.model.declaration

import khelp.utilities.extensions.regularExpression
import khelp.utilities.regex.RegularExpression
import khelp.utilities.regex.WHITE_SPACE

enum class ClassType constructor(internal val regularExpression:RegularExpression)
{
    CLASS("class".regularExpression),
    FUNCTIONAL_INTERFACE("fun".regularExpression + WHITE_SPACE.oneOrMore() + "interface"),
    INTERFACE("interface".regularExpression)
}

fun parseClassType(serialized:String) : ClassType
{
    val trimmed = serialized.trim()

    for(classType in ClassType.values())
    {
        if(classType.regularExpression.matches(trimmed))
        {
            return classType
        }
    }

    throw IllegalArgumentException("Not a valid class type : $serialized")
}
