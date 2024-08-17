package khelp.math.matrix

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MatrixTests
{
    @Test
    fun determinantTest()
    {
        var matrix = Matrix(5, 5, false)

        for (y in 0 until 5)
        {
            for (x in 0 until 5)
            {
                matrix[x, y] = if (x == y) x + 1.0 else 0.0
            }
        }

        Assertions.assertEquals(120.0, matrix.determinant(), Matrix.PRECISION)

        val angle = Math.random() * 2.0 * PI
        val cosAngle = cos(angle)
        val sinAngle = sin(angle)
        matrix = Matrix(3, 3)
        matrix[0, 0] = cosAngle
        matrix[0, 1] = -sinAngle
        matrix[1, 0] = sinAngle
        matrix[1, 1] = cosAngle
        matrix[2, 2] = 1.0
        Assertions.assertEquals(1.0, matrix.determinant(), Matrix.PRECISION)
    }
}