package khelp.neural

import khelp.utilities.argumentCheck

class Vector(val size : Int)
{
    val vector = DoubleArray(this.size)

    operator fun minus(vector : Vector) : Vector
    {
        argumentCheck(
            this.size == vector.size) { "Vectors must have same size. This size=${this.size} given vector size = ${vector.size}" }

        val subtract = Vector(this.size)

        for (index in 0 until this.size)
        {
            subtract.vector[index] = this.vector[index] - vector.vector[index]
        }

        return subtract
    }

    operator fun invoke(function : (Double) -> Double)
    {
        for (index in 0 until this.size)
        {
            this.vector[index] = function(this.vector[index])
        }
    }

    override fun toString() : String
    {
        val stringBuilder = StringBuilder()

        stringBuilder.append("[")
        stringBuilder.append(MatrixPanel.NUMBER.format(this.vector[0]))

        for (index in 1 until this.size)
        {
            stringBuilder.append(", ")
            stringBuilder.append(MatrixPanel.NUMBER.format(this.vector[index]))
        }

        stringBuilder.append("]")

        return stringBuilder.toString()
    }
}