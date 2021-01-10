package khelp.kotlinspector.model.expression.operator

enum class AsOperator(val symbol: String)
{
    AS("as"),
    AS_NULLABLE("as?");

    companion object
    {
        fun parse(serialized: String): AsOperator
        {
            for (operator in AsOperator.values())
            {
                if (serialized == operator.symbol)
                {
                    return operator
                }
            }

            throw IllegalArgumentException("$serialized not represents a valid AsOperator")
        }
    }
}
