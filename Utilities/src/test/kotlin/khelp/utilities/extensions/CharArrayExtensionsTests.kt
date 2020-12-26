package khelp.utilities.extensions

import khelp.utilities.regex.ANY
import khelp.utilities.text.EmptyCharactersInterval
import khelp.utilities.text.SimpleCharactersInterval
import khelp.utilities.text.UnionCharactersInterval
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CharArrayExtensionsTests
{
    @Test
    fun stringTest()
    {
        val array = charArrayOf('L', 'O', 'C', 'K')
        Assertions.assertEquals(">{L | O | C | K}<",
                                array.string(">{", " | ", "}<"))
    }

    @Test
    fun sameTest()
    {
        val array = charArrayOf('L', 'O', 'C', 'K')
        var compare = charArrayOf('L', 'O', 'C', 'K')
        Assertions.assertTrue(array.same(compare))
        compare = charArrayOf('L', 'O', 'C', 'K', 'Y')
        Assertions.assertFalse(array.same(compare))
        compare = charArrayOf('L', 'O', 'O', 'K')
        Assertions.assertFalse(array.same(compare))
    }

    @Test
    fun intervalTest()
    {
        Assertions.assertEquals(EmptyCharactersInterval, CharArray(0).interval)
        var interval = charArrayOf('D').interval
        var simpleInterval = interval as SimpleCharactersInterval
        Assertions.assertEquals('D', simpleInterval.minimum)
        Assertions.assertEquals('D', simpleInterval.maximum)
        interval = charArrayOf('D', 'B').interval
        val union = interval as UnionCharactersInterval
        simpleInterval = union.simpleIntervals[0]
        Assertions.assertEquals('B', simpleInterval.minimum)
        Assertions.assertEquals('B', simpleInterval.maximum)
        simpleInterval = union.simpleIntervals[1]
        Assertions.assertEquals('D', simpleInterval.minimum)
        Assertions.assertEquals('D', simpleInterval.maximum)
        interval = charArrayOf('D', 'B', 'C').interval
        simpleInterval = interval as SimpleCharactersInterval
        Assertions.assertEquals('B', simpleInterval.minimum)
        Assertions.assertEquals('D', simpleInterval.maximum)
    }

    @Test
    fun ignoreCaseIntervalTest()
    {
        Assertions.assertEquals(EmptyCharactersInterval, CharArray(0).ignoreCaseInterval)
        var interval = charArrayOf('D').ignoreCaseInterval
        var union = interval as UnionCharactersInterval
        var simpleInterval = union.simpleIntervals[0]
        Assertions.assertEquals('D', simpleInterval.minimum)
        Assertions.assertEquals('D', simpleInterval.maximum)
        simpleInterval = union.simpleIntervals[1]
        Assertions.assertEquals('d', simpleInterval.minimum)
        Assertions.assertEquals('d', simpleInterval.maximum)
        interval = charArrayOf('D', 'B').ignoreCaseInterval
        union = interval as UnionCharactersInterval
        simpleInterval = union.simpleIntervals[0]
        Assertions.assertEquals('B', simpleInterval.minimum)
        Assertions.assertEquals('B', simpleInterval.maximum)
        simpleInterval = union.simpleIntervals[1]
        Assertions.assertEquals('D', simpleInterval.minimum)
        Assertions.assertEquals('D', simpleInterval.maximum)
        simpleInterval = union.simpleIntervals[2]
        Assertions.assertEquals('b', simpleInterval.minimum)
        Assertions.assertEquals('b', simpleInterval.maximum)
        simpleInterval = union.simpleIntervals[3]
        Assertions.assertEquals('d', simpleInterval.minimum)
        Assertions.assertEquals('d', simpleInterval.maximum)
        interval = charArrayOf('D', 'B', 'C').ignoreCaseInterval
        union = interval as UnionCharactersInterval
        simpleInterval = union.simpleIntervals[0]
        Assertions.assertEquals('B', simpleInterval.minimum)
        Assertions.assertEquals('D', simpleInterval.maximum)
        simpleInterval = union.simpleIntervals[1]
        Assertions.assertEquals('b', simpleInterval.minimum)
        Assertions.assertEquals('d', simpleInterval.maximum)
    }


    @Test
    fun regularExpressionTest()
    {
        val arrayKT = charArrayOf('K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T')
        val regex = arrayKT.regularExpression
        Assertions.assertTrue(regex.matches("M"))
        Assertions.assertFalse(regex.matches("W"))
    }

    @Test
    fun allCharactersExcludeThoseTest()
    {
        val arrayKT = charArrayOf('K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T')
        val regex = arrayKT.allCharactersExcludeThose
        Assertions.assertFalse(regex.matches("M"))
        Assertions.assertTrue(regex.matches("W"))
    }

    @Test
    fun plusRegularExpressionTest()
    {
        val arrayKT = charArrayOf('K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T')
        var regex = arrayKT + ANY
        Assertions.assertTrue(regex.matches("KO"))
        Assertions.assertTrue(regex.matches("LA"))
        Assertions.assertTrue(regex.matches("OK"))
        Assertions.assertFalse(regex.matches("AT"))

        val group = ANY.group()
        regex = arrayKT + group
        var matcher = regex.matcher("KO")
        Assertions.assertTrue(matcher.matches())
        Assertions.assertEquals("O", matcher.group(group))
        matcher = regex.matcher("LA")
        Assertions.assertTrue(matcher.matches())
        Assertions.assertEquals("A", matcher.group(group))
        matcher = regex.matcher("OK")
        Assertions.assertTrue(matcher.matches())
        Assertions.assertEquals("K", matcher.group(group))
        matcher = regex.matcher("AT")
        Assertions.assertFalse(matcher.matches())
    }

    @Test
    fun orRegularExpressionTest()
    {
        val arrayKT = charArrayOf('K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T')
        var regex = arrayKT OR 'Y'.regularExpression
        Assertions.assertTrue(regex.matches("K"))
        Assertions.assertTrue(regex.matches("L"))
        Assertions.assertTrue(regex.matches("Y"))
        Assertions.assertFalse(regex.matches("A"))

        val group = 'Y'.regularExpression.group()
        regex = arrayKT OR group
        var matcher = regex.matcher("K")
        Assertions.assertTrue(matcher.matches())
        Assertions.assertEquals("", matcher.group(group))
        matcher = regex.matcher("L")
        Assertions.assertTrue(matcher.matches())
        Assertions.assertEquals("", matcher.group(group))
        matcher = regex.matcher("Y")
        Assertions.assertTrue(matcher.matches())
        Assertions.assertEquals("Y", matcher.group(group))
        matcher = regex.matcher("A")
        Assertions.assertFalse(matcher.matches())
    }

    @Test
    fun zeroOrMoreTest()
    {
        val arrayKT = charArrayOf('K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T')
        val regex = arrayKT.zeroOrMore()
        Assertions.assertTrue(regex.matches(""))
        Assertions.assertTrue(regex.matches("L"))
        Assertions.assertTrue(regex.matches("KO"))
        Assertions.assertTrue(regex.matches("OK"))
        Assertions.assertTrue(regex.matches("LOOK"))
        Assertions.assertFalse(regex.matches("WOK"))
        Assertions.assertFalse(regex.matches("KNOW"))
    }

    @Test
    fun oneOrMoreTest()
    {
        val arrayKT = charArrayOf('K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T')
        val regex = arrayKT.oneOrMore()
        Assertions.assertFalse(regex.matches(""))
        Assertions.assertTrue(regex.matches("L"))
        Assertions.assertTrue(regex.matches("KO"))
        Assertions.assertTrue(regex.matches("OK"))
        Assertions.assertTrue(regex.matches("LOOK"))
        Assertions.assertFalse(regex.matches("WOK"))
        Assertions.assertFalse(regex.matches("KNOW"))
    }

    @Test
    fun zeroOrOneTest()
    {
        val arrayKT = charArrayOf('K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T')
        val regex = arrayKT.zeroOrOne()
        Assertions.assertTrue(regex.matches(""))
        Assertions.assertTrue(regex.matches("L"))
        Assertions.assertFalse(regex.matches("KO"))
        Assertions.assertFalse(regex.matches("Y"))
    }

    @Test
    fun exactTimesTest()
    {
        val arrayKT = charArrayOf('K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T')
        val regex = arrayKT.exactTimes(3)
        Assertions.assertFalse(regex.matches(""))
        Assertions.assertFalse(regex.matches("L"))
        Assertions.assertFalse(regex.matches("LO"))
        Assertions.assertTrue(regex.matches("LOL"))
        Assertions.assertFalse(regex.matches("KOOL"))
    }

    @Test
    fun atLeastTest()
    {
        val arrayKT = charArrayOf('K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T')
        val regex = arrayKT.atLeast(3)
        Assertions.assertFalse(regex.matches(""))
        Assertions.assertFalse(regex.matches("L"))
        Assertions.assertFalse(regex.matches("LO"))
        Assertions.assertTrue(regex.matches("LOL"))
        Assertions.assertTrue(regex.matches("KOOL"))
    }

    @Test
    fun atMostTest()
    {
        val arrayKT = charArrayOf('K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T')
        val regex = arrayKT.atMost(3)
        Assertions.assertTrue(regex.matches(""))
        Assertions.assertTrue(regex.matches("L"))
        Assertions.assertTrue(regex.matches("LO"))
        Assertions.assertTrue(regex.matches("LOL"))
        Assertions.assertFalse(regex.matches("KOOL"))
    }

    @Test
    fun betweenTest()
    {
        val arrayKT = charArrayOf('K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T')
        val regex = arrayKT.between(3, 5)
        Assertions.assertFalse(regex.matches(""))
        Assertions.assertFalse(regex.matches("L"))
        Assertions.assertFalse(regex.matches("LO"))
        Assertions.assertTrue(regex.matches("LOL"))
        Assertions.assertTrue(regex.matches("KOOL"))
        Assertions.assertTrue(regex.matches("POKOP"))
        Assertions.assertFalse(regex.matches("POOKOL"))
    }
}