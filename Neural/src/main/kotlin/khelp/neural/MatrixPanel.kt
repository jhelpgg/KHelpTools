package khelp.neural

import java.math.RoundingMode
import java.text.NumberFormat

class MatrixPanel(private val matrix : Matrix)
{
    companion object
    {
        val NUMBER : NumberFormat by lazy {
            val format = NumberFormat.getNumberInstance()
            format.minimumFractionDigits = 3
            format.maximumFractionDigits = 3
            format.roundingMode = RoundingMode.HALF_UP
            format
        }
    }
}