package khelp.kotlinspector.model.expression.operator

enum class PrefixUnaryOperator(val symbol: String)
{
    INCREMENT("++"),
    DECREMENT("--"),
    NOT("!"),
    NONE("");

    companion object
    {
        fun parse(serialized: String): PrefixUnaryOperator
        {
            for (operator in PrefixUnaryOperator.values())
            {
                if (serialized == operator.symbol)
                {
                    return operator
                }
            }

            return PrefixUnaryOperator.NONE
        }
    }
}
