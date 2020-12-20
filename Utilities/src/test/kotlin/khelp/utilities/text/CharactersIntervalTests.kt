package khelp.utilities.text

import khelp.utilities.extensions.interval
import khelp.utilities.extensions.plus
import org.junit.Assert
import org.junit.Test

class CharactersIntervalTests
{
    @Test
    fun charactersTest()
    {
        Assert.assertEquals("{K}", 'K'.interval.toString())
        Assert.assertEquals("{C} U {K}", ('K'.interval + 'C').toString())
        Assert.assertEquals("[A, E]", charArrayOf('D', 'A', 'D', 'E', 'C', 'B').interval.toString())
        Assert.assertEquals("[A, E] U [X, Z]",
                            charArrayOf('D', 'A', 'Y', 'D', 'E', 'C', 'B', 'X', 'Z').interval.toString())
    }

    @Test
    fun unionTest()
    {
        val interval1 = interval('D', 'K')
        Assert.assertEquals("[D, K]", interval1.toString())
        val interval2 = interval('R', 'W')
        Assert.assertEquals("[R, W]", interval2.toString())
        val union1 = interval1 + interval2
        Assert.assertEquals("[D, K] U [R, W]", union1.toString())
        val interval3 = interval('F', 'T')
        Assert.assertEquals("[F, T]", interval3.toString())
        val union2 = union1 + interval3
        Assert.assertEquals("[D, W]", union2.toString())
        Assert.assertTrue(union2 is SimpleCharactersInterval)
    }
}