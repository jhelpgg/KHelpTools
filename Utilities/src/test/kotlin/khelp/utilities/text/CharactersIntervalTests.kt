package khelp.utilities.text

import khelp.utilities.extensions.interval
import khelp.utilities.extensions.plus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CharactersIntervalTests
{
    @Test
    fun charactersTest()
    {
        Assertions.assertEquals("{K}", 'K'.interval.toString())
        Assertions.assertEquals("{C} U {K}", ('K'.interval + 'C').toString())
        Assertions.assertEquals("[A, E]", charArrayOf('D', 'A', 'D', 'E', 'C', 'B').interval.toString())
        Assertions.assertEquals("[A, E] U [X, Z]",
                            charArrayOf('D', 'A', 'Y', 'D', 'E', 'C', 'B', 'X', 'Z').interval.toString())
    }

    @Test
    fun unionTest()
    {
        val interval1 = interval('D', 'K')
        Assertions.assertEquals("[D, K]", interval1.toString())
        val interval2 = interval('R', 'W')
        Assertions.assertEquals("[R, W]", interval2.toString())
        val union1 = interval1 + interval2
        Assertions.assertEquals("[D, K] U [R, W]", union1.toString())
        val interval3 = interval('F', 'T')
        Assertions.assertEquals("[F, T]", interval3.toString())
        val union2 = union1 + interval3
        Assertions.assertEquals("[D, W]", union2.toString())
        Assertions.assertTrue(union2 is SimpleCharactersInterval)
    }
}