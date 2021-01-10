package khelp.kotlinspector.model.expression.operator

enum class PostfixUnaryOperator(val symbol: String)
{
    INCREMENT("++"),
    DECREMENT("--"),
    ASSUME_NOT_NULL("!!"),
    NONE("");

    companion object
    {
        fun parse(serialized: String): PostfixUnaryOperator
        {
            for (operator in PostfixUnaryOperator.values())
            {
                if (serialized == operator.symbol)
                {
                    return operator
                }
            }

            return PostfixUnaryOperator.NONE
        }
    }
}
