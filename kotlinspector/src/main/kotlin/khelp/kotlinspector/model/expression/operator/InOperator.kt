package khelp.kotlinspector.model.expression.operator

enum class InOperator(val symbol: String)
{
    IN("in"),
    NOT_IN("!in"),
    NONE("");

    companion object
    {
        fun parse(serialized: String): InOperator
        {
            for (operator in InOperator.values())
            {
                if (serialized == operator.symbol)
                {
                    return operator
                }
            }

            return InOperator.NONE
        }
    }
}
