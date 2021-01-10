package khelp.kotlinspector.model.expression.operator

enum class MemberAccessOperator(val symbol: String)
{
    CALL("."),
    NULLABLE_CALL("?."),
    METHOD_REFERENCE("::"),
    NONE("");

    companion object
    {
        fun parse(serialized: String): MemberAccessOperator
        {
            for (operator in MemberAccessOperator.values())
            {
                if (serialized == operator.symbol)
                {
                    return operator
                }
            }

            return MemberAccessOperator.NONE
        }
    }
}
