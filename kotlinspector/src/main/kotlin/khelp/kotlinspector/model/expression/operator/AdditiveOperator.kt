package khelp.kotlinspector.model.expression.operator

enum class AdditiveOperator(val symbol: String)
{
    PLUS("+"),
    MINUS("-");

    companion object
    {
        fun parse(serialized: String): AdditiveOperator
        {
            for (operator in AdditiveOperator.values())
            {
                if (serialized == operator.symbol)
                {
                    return operator
                }
            }

            throw IllegalArgumentException("$serialized not represents a valid AdditiveOperator")
        }
    }
}
