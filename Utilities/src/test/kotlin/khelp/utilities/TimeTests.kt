package khelp.utilities

import khelp.utilities.extensions.minutes
import khelp.utilities.extensions.seconds
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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
    }
}
