package khelp.kotlinspector.model.expression.operator

enum class IsOperator(val symbol: String)
{
    IS("is"),
    NOT_IS("!is"),
    NONE("");

    companion object
    {
        fun parse(serialized: String): IsOperator
        {
            for (operator in IsOperator.values())
            {
                if (serialized == operator.symbol)
                {
                    return operator
                }
            }

            return IsOperator.NONE
        }
    }
}
