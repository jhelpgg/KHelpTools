package khelp.neural

import khelp.utilities.argumentCheck

class Matrix(val numberEnter : Int, val numberExit : Int)
{
    val matrix = DoubleArray(this.numberEnter * this.numberExit) { Math.random() }

    fun apply(vector : Vector, result : Vector = Vector(this.numberExit), function : (Double) -> Double) : Vector
    {
        argumentCheck(vector.size == this.numberEnter)
        { "The vector size is ${vector.size} not corresponds to number entries ${this.numberEnter}" }
        argumentCheck(result.size == this.numberExit)
        { "The vector result size is ${result.size} not corresponds to number exit ${this.numberExit}" }

        var index = 0

        for (indexExit in 0 until this.numberExit)
        {
            var value = 0.0

            for (indexEnter in 0 until this.numberEnter)
            {
                value += this.matrix[index] * vector.vector[indexEnter]
                index ++
            }

            result.vector[indexExit] = function(value)
        }

        return result
    }

    fun transpose() : Matrix
    {
        val transpose = Matrix(this.numberExit, this.numberEnter)
        var index = 0
        var columnTranspose = 0

        while (index < this.matrix.size)
        {
            var indexTranspose = columnTranspose

            for (y in 0 until this.numberEnter)
            {
                transpose.matrix[indexTranspose] = this.matrix[index]
                index ++
                indexTranspose += this.numberExit
            }

            columnTranspose ++
        }

        return transpose
    }

    override fun toString() : String
    {
        val stringBuilder = StringBuilder()
        stringBuilder.append("[\n")

        var index = 0

        for (line in 0 until this.numberEnter)
        {
            stringBuilder.append(" [")
            stringBuilder.append(MatrixPanel.NUMBER.format(this.matrix[index]))
            index ++

            for (column in 1 until this.numberExit)
            {
                stringBuilder.append(", ")
                stringBuilder.append(MatrixPanel.NUMBER.format(this.matrix[index]))
                index ++
            }

            stringBuilder.append(" ]\n")
        }

        stringBuilder.append("]")

        return stringBuilder.toString()
    }
}