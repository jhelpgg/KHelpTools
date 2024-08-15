package khelp.utilities.interval

import khelp.utilities.text.INFINITE_CHARACTER
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IntervalTests
{
    fun testEmpty()
    {
        var interval = interval<String> { }
        Assertions.assertTrue(interval.empty)
        Assertions.assertFalse(interval.notEmpty)

        interval = interval<String> {
            "C".include.."G".include
        }
        Assertions.assertFalse(interval.empty)
        Assertions.assertTrue(interval.notEmpty)

        interval = interval<String> {
            "G".include.."C".include
        }
        Assertions.assertTrue(interval.empty)
        Assertions.assertFalse(interval.notEmpty)
    }

    @Test
    fun testUnion()
    {
        var union = interval<Int> {
            2.include..6.include
            0.include..5.include
            2.include..4.include
            7.exclude..9.include
        }

        Assertions.assertTrue(union is IntervalUnion<Int>,
                              "Union must be a ${IntervalUnion::class.java.name} not ${union::class.java.name}")

        var intervals = (union as IntervalUnion<Int>).intervals
        Assertions.assertEquals(2, intervals.size)
        Assertions.assertEquals(interval<Int> { 0.include..6.include }, intervals[0])
        Assertions.assertEquals(interval<Int> { 7.exclude..9.include }, intervals[1])

        union = union union interval { 50.include..infinite }
        Assertions.assertTrue(union is IntervalUnion<Int>,
                              "Union must be a ${IntervalUnion::class.java.name} not ${union::class.java.name}")

        intervals = (union as IntervalUnion<Int>).intervals
        Assertions.assertEquals(3, intervals.size)
        Assertions.assertEquals(interval<Int> { 0.include..6.include }, intervals[0])
        Assertions.assertEquals(interval<Int> { 7.exclude..9.include }, intervals[1])
        Assertions.assertEquals(interval<Int> { 50.include..infinite }, intervals[2])

        union = union union interval<Int> { infinite..(-5).exclude }
        Assertions.assertTrue(union is IntervalUnion<Int>,
                              "Union must be a ${IntervalUnion::class.java.name} not ${union::class.java.name}")

        intervals = (union as IntervalUnion<Int>).intervals
        Assertions.assertEquals(4, intervals.size)
        Assertions.assertEquals(interval<Int> { infinite..(-5).exclude }, intervals[0])
        Assertions.assertEquals(interval<Int> { 0.include..6.include }, intervals[1])
        Assertions.assertEquals(interval<Int> { 7.exclude..9.include }, intervals[2])
        Assertions.assertEquals(interval<Int> { 50.include..infinite }, intervals[3])

        union = union union interval<Int> { (-6).exclude..8.exclude }
        Assertions.assertTrue(union is IntervalUnion<Int>,
                              "Union must be a ${IntervalUnion::class.java.name} not ${union::class.java.name}")

        intervals = (union as IntervalUnion<Int>).intervals
        Assertions.assertEquals(2, intervals.size)
        Assertions.assertEquals(interval<Int> { infinite..9.include }, intervals[0])
        Assertions.assertEquals(interval<Int> { 50.include..infinite }, intervals[1])

        union = union union interval<Int> { 5.exclude..88.include }
        Assertions.assertTrue(union is IntervalSimple<Int>,
                              "Union must be a ${IntervalSimple::class.java.name} not ${union::class.java.name}")
        Assertions.assertEquals(interval<Int> { infinite..infinite }, union)
    }

    @Test
    fun testIntersect()
    {
        val interval1 =
            interval<Int> {
                (-22).include..(-6).include
                0.include..5.include
                7.exclude..99.include
            }

        val interval2 =
            interval<Int> {
                (-5).include..(-3).include
                2.include..12.exclude
                42.include..73.include
            }

        val expected =
            interval<Int> {
                2.include..5.include
                7.exclude..12.exclude
                42.include..73.include
            }

        Assertions.assertEquals(expected, interval1 intersect interval2)
    }

    @Test
    fun testToString()
    {
        val interval =
            interval<Int> {
                infinite..(-42).include
                (-5).include..(-3).include
                2.include..12.exclude
                42.include..73.include
                85.include..85.include
                99.exclude..infinite
            }
        Assertions.assertEquals("]$INFINITE_CHARACTER, -42] U [-5, -3] U [2, 12[ U [42, 73] U {85} U ]99, $INFINITE_CHARACTER[",
                                interval.toString())

        Assertions.assertEquals("{}", interval<Double> {}.toString())
    }
}
