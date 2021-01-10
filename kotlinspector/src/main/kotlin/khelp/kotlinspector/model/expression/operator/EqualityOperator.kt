package khelp.kotlinspector.model.expression.operator

enum class EqualityOperator(val symbol: String)
{
    NOT_EQUALS("!="),
    NOT_SAME("!=="),
    EQUALS("=="),
    SAME("===");

    companion object
    {
        fun parse(serialized: String): EqualityOperator
        {
            for (operator in EqualityOperator.values())
            {
                if (serialized == operator.symbol)
                {
                    return operator
                }
            }

            throw IllegalArgumentException("$serialized not represents a valid EqualityOperator")
        }
    }
}
