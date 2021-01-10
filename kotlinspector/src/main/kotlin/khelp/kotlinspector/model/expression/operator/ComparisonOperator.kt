package khelp.kotlinspector.model.expression.operator

enum class ComparisonOperator(val symbol: String)
{
    LOWER_EQUAL("<="),
    LOWER("<"),
    UPPER_EQUAL(">="),
    UPPER(">");

    companion object
    {
        fun parse(serialized: String): ComparisonOperator
        {
            for (operator in ComparisonOperator.values())
            {
                if (serialized == operator.symbol)
                {
                    return operator
                }
            }

            throw IllegalArgumentException("$serialized not represents a valid ComparisonOperator")
        }
    }
}
