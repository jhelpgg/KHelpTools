package khelp.utilities

import khelp.utilities.extensions.days
import khelp.utilities.extensions.hours
import khelp.utilities.extensions.minutes
import khelp.utilities.extensions.seconds
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Objects

class TimeTests
{
    @Test
    fun timeTests()
    {
        val time = 3.minutes + 45.seconds + 73
        Assertions.assertEquals("3 minutes 45 seconds", time.toString())
        Assertions.assertEquals((3 * 60L + 45L) * 1000L + 73L, time.milliseconds)
        Assertions.assertEquals(73, time.partMilliseconds)
        Assertions.assertEquals(3 * 60L + 45L, time.seconds)
        Assertions.assertEquals(45, time.partSeconds)
        Assertions.assertEquals(3L, time.minutes)
        Assertions.assertEquals(3, time.partMinutes)
        Assertions.assertEquals(0L, time.hours)
        Assertions.assertEquals(0, time.partHours)
        Assertions.assertEquals(0L, time.days)

        val time2 = time - 45.seconds
        Assertions.assertEquals("3 minutes 73 milliseconds", time2.toString())
        Assertions.assertEquals(3 * 60L * 1000L + 73L, time2.milliseconds)
        Assertions.assertEquals(73, time2.partMilliseconds)
        Assertions.assertEquals(3 * 60L, time2.seconds)
        Assertions.assertEquals(0, time2.partSeconds)
        Assertions.assertEquals(3L, time2.minutes)
        Assertions.assertEquals(3, time2.partMinutes)
        Assertions.assertEquals(0L, time2.hours)
        Assertions.assertEquals(0, time2.partHours)
        Assertions.assertEquals(0L, time2.days)

        val time3 = time2 - (2.minutes + 59.seconds)
        Assertions.assertEquals("1 second 73 milliseconds", time3.toString())
        Assertions.assertEquals(1073L, time3.milliseconds)
        Assertions.assertEquals(73, time3.partMilliseconds)
        Assertions.assertEquals(1L, time3.seconds)
        Assertions.assertEquals(1, time3.partSeconds)
        Assertions.assertEquals(0L, time3.minutes)
        Assertions.assertEquals(0, time3.partMinutes)
        Assertions.assertEquals(0L, time3.hours)
        Assertions.assertEquals(0, time3.partHours)
        Assertions.assertEquals(0L, time3.days)

        val time4 = time3 - 1.seconds
        Assertions.assertEquals("73 milliseconds", time4.toString())
        Assertions.assertEquals(73L, time4.milliseconds)
        Assertions.assertEquals(73, time4.partMilliseconds)
        Assertions.assertEquals(0L, time4.seconds)
        Assertions.assertEquals(0, time4.partSeconds)
        Assertions.assertEquals(0L, time4.minutes)
        Assertions.assertEquals(0, time4.partMinutes)
        Assertions.assertEquals(0L, time4.hours)
        Assertions.assertEquals(0, time4.partHours)
        Assertions.assertEquals(0L, time4.days)

        val time5 = time4 - 73
        Assertions.assertEquals("0 millisecond", time5.toString())
        Assertions.assertEquals(0L, time5.milliseconds)
        Assertions.assertEquals(0, time5.partMilliseconds)
        Assertions.assertEquals(0L, time5.seconds)
        Assertions.assertEquals(0, time5.partSeconds)
        Assertions.assertEquals(0L, time5.minutes)
        Assertions.assertEquals(0, time5.partMinutes)
        Assertions.assertEquals(0L, time5.hours)
        Assertions.assertEquals(0, time5.partHours)
        Assertions.assertEquals(0L, time5.days)

        val time6 = Time(1024)
        Assertions.assertEquals("1 second 24 milliseconds", time6.toString())
        Assertions.assertEquals(1024L, time6.milliseconds)
        Assertions.assertEquals(24, time6.partMilliseconds)
        Assertions.assertEquals(1L, time6.seconds)
        Assertions.assertEquals(1, time6.partSeconds)
        Assertions.assertEquals(0L, time6.minutes)
        Assertions.assertEquals(0, time6.partMinutes)
        Assertions.assertEquals(0L, time6.hours)
        Assertions.assertEquals(0, time6.partHours)
        Assertions.assertEquals(0L, time6.days)

        val time7 = 3.days + 3.hours
        Assertions.assertEquals("3 days 3 hours", time7.toString())
        Assertions.assertEquals((3L * 24L + 3L) * 60L * 60L * 1000L, time7.milliseconds)
        Assertions.assertEquals(0, time7.partMilliseconds)
        Assertions.assertEquals((3L * 24L + 3L) * 60L * 60L, time7.seconds)
        Assertions.assertEquals(0, time7.partSeconds)
        Assertions.assertEquals((3L * 24L + 3L) * 60L, time7.minutes)
        Assertions.assertEquals(0, time7.partMinutes)
        Assertions.assertEquals(3L * 24L + 3L, time7.hours)
        Assertions.assertEquals(3, time7.partHours)
        Assertions.assertEquals(3L, time7.days)

        val time8 = 5.hours + 42.minutes
        Assertions.assertEquals("5 hours 42 minutes", time8.toString())
        Assertions.assertEquals((5L * 60L + 42L) * 60L * 1000L, time8.milliseconds)
        Assertions.assertEquals(0, time8.partMilliseconds)
        Assertions.assertEquals((5L * 60L + 42L) * 60L, time8.seconds)
        Assertions.assertEquals(0, time8.partSeconds)
        Assertions.assertEquals(5L * 60L + 42L, time8.minutes)
        Assertions.assertEquals(42, time8.partMinutes)
        Assertions.assertEquals(5L, time8.hours)
        Assertions.assertEquals(5, time8.partHours)
        Assertions.assertEquals(0L, time8.days)

        val time9 = 3.seconds
        Assertions.assertEquals("3 seconds", time9.toString())
        Assertions.assertEquals(3L * 1000L, time9.milliseconds)
        Assertions.assertEquals(0, time9.partMilliseconds)
        Assertions.assertEquals(3L, time9.seconds)
        Assertions.assertEquals(3, time9.partSeconds)
        Assertions.assertEquals(0L, time9.minutes)
        Assertions.assertEquals(0, time9.partMinutes)
        Assertions.assertEquals(0L, time9.hours)
        Assertions.assertEquals(0, time9.partHours)
        Assertions.assertEquals(0L, time9.days)

        val time10 = time9 - 159L
        Assertions.assertEquals("2 seconds 841 milliseconds", time10.toString())
        Assertions.assertEquals(2L * 1000L + 841L, time10.milliseconds)
        Assertions.assertEquals(841, time10.partMilliseconds)
        Assertions.assertEquals(2L, time10.seconds)
        Assertions.assertEquals(2, time10.partSeconds)
        Assertions.assertEquals(0L, time10.minutes)
        Assertions.assertEquals(0, time10.partMinutes)
        Assertions.assertEquals(0L, time10.hours)
        Assertions.assertEquals(0, time10.partHours)
        Assertions.assertEquals(0L, time10.days)

        val time11 = time10 + 159L
        Assertions.assertEquals("3 seconds", time11.toString())
        Assertions.assertEquals(3L * 1000L, time11.milliseconds)
        Assertions.assertEquals(0, time11.partMilliseconds)
        Assertions.assertEquals(3L, time11.seconds)
        Assertions.assertEquals(3, time11.partSeconds)
        Assertions.assertEquals(0L, time11.minutes)
        Assertions.assertEquals(0, time11.partMinutes)
        Assertions.assertEquals(0L, time11.hours)
        Assertions.assertEquals(0, time11.partHours)
        Assertions.assertEquals(0L, time11.days)

        val time12 = 42.seconds + 73
        Assertions.assertTrue(time12.equals(time12))
        Assertions.assertFalse(time12.equals(null))
        Assertions.assertTrue(time12.equals(42073))
        Assertions.assertTrue(time12.equals(42073L))
        Assertions.assertFalse(time12.equals(time11))
        Assertions.assertFalse(time12.equals("test"))
        Assertions.assertEquals(Objects.hash(42073L), time12.hashCode())
        Assertions.assertEquals(0, time12.compareTo(time12))
        Assertions.assertTrue(time12 > time11)
        Assertions.assertFalse(time12 < time11)
    }
}
