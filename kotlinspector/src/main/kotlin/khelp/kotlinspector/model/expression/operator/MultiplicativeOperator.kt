package khelp.kotlinspector.model.expression.operator

enum class MultiplicativeOperator(val symbol: String)
{
    TIMES("*"),
    DIVIDE("/"),
    REMAINDER("%");

    companion object
    {
        fun parse(serialized: String): MultiplicativeOperator
        {
            for (operator in MultiplicativeOperator.values())
            {
                if (serialized == operator.symbol)
                {
                    return operator
                }
            }

            throw IllegalArgumentException("$serialized not represents a valid MultiplicativeOperator")
        }
    }
}
