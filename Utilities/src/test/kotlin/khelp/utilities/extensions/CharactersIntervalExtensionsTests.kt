package khelp.utilities.extensions

import khelp.utilities.regex.ANY
import khelp.utilities.text.EmptyCharactersInterval
import khelp.utilities.text.SimpleCharactersInterval
import khelp.utilities.text.UnionCharactersInterval
import khelp.utilities.text.interval
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CharactersIntervalExtensionsTests
{
    @Test
    fun plusIntervalTest()
    {
        Assertions.assertEquals(EmptyCharactersInterval, EmptyCharactersInterval + EmptyCharactersInterval)

        val intervalAF = interval('A', 'F')
        Assertions.assertEquals(intervalAF, intervalAF + EmptyCharactersInterval)
        Assertions.assertEquals(intervalAF, EmptyCharactersInterval + intervalAF )

        val intervalEU = interval('E', 'U')
        var intervalAU = intervalAF + intervalEU
        Assertions.assertTrue(intervalAU is SimpleCharactersInterval)
        var simpleCharactersInterval = intervalAU as SimpleCharactersInterval
        Assertions.assertEquals('A',simpleCharactersInterval.minimum)
        Assertions.assertEquals('U',simpleCharactersInterval.maximum)
        intervalAU =  intervalEU + intervalAF
        Assertions.assertTrue(intervalAU is SimpleCharactersInterval)
         simpleCharactersInterval = intervalAU as SimpleCharactersInterval
        Assertions.assertEquals('A',simpleCharactersInterval.minimum)
        Assertions.assertEquals('U',simpleCharactersInterval.maximum)

        val intervalLR = interval('L', 'R')
        var intervalAF_LR = intervalAF + intervalLR
        Assertions.assertTrue(intervalAF_LR is UnionCharactersInterval)
        var unionCharactersInterval = intervalAF_LR as UnionCharactersInterval
        Assertions.assertEquals('A', unionCharactersInterval.simpleIntervals[0].minimum)
        Assertions.assertEquals('F', unionCharactersInterval.simpleIntervals[0].maximum)
        Assertions.assertEquals('L', unionCharactersInterval.simpleIntervals[1].minimum)
        Assertions.assertEquals('R', unionCharactersInterval.simpleIntervals[1].maximum)
        intervalAF_LR =  intervalLR + intervalAF
        Assertions.assertTrue(intervalAF_LR is UnionCharactersInterval)
         unionCharactersInterval = intervalAF_LR as UnionCharactersInterval
        Assertions.assertEquals('A', unionCharactersInterval.simpleIntervals[0].minimum)
        Assertions.assertEquals('F', unionCharactersInterval.simpleIntervals[0].maximum)
        Assertions.assertEquals('L', unionCharactersInterval.simpleIntervals[1].minimum)
        Assertions.assertEquals('R', unionCharactersInterval.simpleIntervals[1].maximum)

        val intervalEZ = intervalEU + interval('V', 'Z')
        val intervalAZ = intervalAF_LR + intervalEZ
        simpleCharactersInterval = intervalAZ as SimpleCharactersInterval
        Assertions.assertEquals('A',simpleCharactersInterval.minimum)
        Assertions.assertEquals('Z',simpleCharactersInterval.maximum)

        val intervalAG = intervalAF + 'G'
        simpleCharactersInterval = intervalAG as SimpleCharactersInterval
        Assertions.assertEquals('A',simpleCharactersInterval.minimum)
        Assertions.assertEquals('G',simpleCharactersInterval.maximum)
    }

    @Test
    fun regularExpressionTest()
    {
        val intervalKT = interval('K', 'T')
        val regex = intervalKT.regularExpression
        Assertions.assertTrue(regex.matches("M"))
        Assertions.assertFalse(regex.matches("W"))
    }

    @Test
    fun allCharactersExcludeThoseTest()
    {
        val intervalKT = interval('K', 'T')
        val regex = intervalKT.allCharactersExcludeThose
        Assertions.assertFalse(regex.matches("M"))
        Assertions.assertTrue(regex.matches("W"))
    }

    @Test
    fun plusRegularExpressionTest()
    {
        val intervalKT = interval('K', 'T')
        var regex = intervalKT + ANY
        Assertions.assertTrue(regex.matches("KO"))
        Assertions.assertTrue(regex.matches("LA"))
        Assertions.assertTrue(regex.matches("OK"))
        Assertions.assertFalse(regex.matches("AT"))

        val group = ANY.group()
        regex = intervalKT + group
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
        val intervalKT = interval('K', 'T')
        var regex = intervalKT OR 'Y'.regularExpression
        Assertions.assertTrue(regex.matches("K"))
        Assertions.assertTrue(regex.matches("L"))
        Assertions.assertTrue(regex.matches("Y"))
        Assertions.assertFalse(regex.matches("A"))

        val group = 'Y'.regularExpression.group()
        regex = intervalKT OR group
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
        val intervalKT = interval('K', 'T')
        val regex = intervalKT.zeroOrMore()
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
        val intervalKT = interval('K', 'T')
        val regex = intervalKT.oneOrMore()
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
        val intervalKT = interval('K', 'T')
        val regex = intervalKT.zeroOrOne()
        Assertions.assertTrue(regex.matches(""))
        Assertions.assertTrue(regex.matches("L"))
        Assertions.assertFalse(regex.matches("KO"))
        Assertions.assertFalse(regex.matches("Y"))
    }

    @Test
    fun exactTimesTest()
    {
        val intervalKT = interval('K', 'T')
        val regex = intervalKT.exactTimes(3)
        Assertions.assertFalse(regex.matches(""))
        Assertions.assertFalse(regex.matches("L"))
        Assertions.assertFalse(regex.matches("LO"))
        Assertions.assertTrue(regex.matches("LOL"))
        Assertions.assertFalse(regex.matches("KOOL"))
    }

    @Test
    fun atLeastTest()
    {
        val intervalKT = interval('K', 'T')
        val regex = intervalKT.atLeast(3)
        Assertions.assertFalse(regex.matches(""))
        Assertions.assertFalse(regex.matches("L"))
        Assertions.assertFalse(regex.matches("LO"))
        Assertions.assertTrue(regex.matches("LOL"))
        Assertions.assertTrue(regex.matches("KOOL"))
    }

    @Test
    fun atMostTest()
    {
        val intervalKT = interval('K', 'T')
        val regex = intervalKT.atMost(3)
        Assertions.assertTrue(regex.matches(""))
        Assertions.assertTrue(regex.matches("L"))
        Assertions.assertTrue(regex.matches("LO"))
        Assertions.assertTrue(regex.matches("LOL"))
        Assertions.assertFalse(regex.matches("KOOL"))
    }

    @Test
    fun betweenTest()
    {
        val intervalKT = interval('K', 'T')
        val regex = intervalKT.between(3,5)
        Assertions.assertFalse(regex.matches(""))
        Assertions.assertFalse(regex.matches("L"))
        Assertions.assertFalse(regex.matches("LO"))
        Assertions.assertTrue(regex.matches("LOL"))
        Assertions.assertTrue(regex.matches("KOOL"))
        Assertions.assertTrue(regex.matches("POKOP"))
        Assertions.assertFalse(regex.matches("POOKOL"))
    }
}