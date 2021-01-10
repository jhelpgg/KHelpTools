package khelp.kotlinspector.model.expression.operator

enum class AssignmentAndOperator(val symbol: String)
{
    PLUS_ASSIGN("+="),
    MINUS_ASSIGN("-="),
    TIMES_ASSIGN("*="),
    DIVIDE_ASSIGN("/="),
    REMAINDER_ASSIGN("%="),
    NONE("");

    companion object
    {
        fun parse(serialized: String): AssignmentAndOperator
        {
            for (operator in AssignmentAndOperator.values())
            {
                if (serialized == operator.symbol)
                {
                    return operator
                }
            }

            return AssignmentAndOperator.NONE
        }
    }
}

