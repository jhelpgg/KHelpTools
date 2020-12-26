package khelp.utilities.extensions

import java.util.Calendar
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CalendarExtensionsTests
{
    @Test
    fun fullStringTest()
    {
        val calendar = Calendar.getInstance()
        calendar.set(1985, Calendar.APRIL, 1, 20, 42, 37)
        calendar.set(Calendar.MILLISECOND, 666)
        Assertions.assertEquals("1985/04/01:20H42M37S666", calendar.fullString())

        calendar.set(1985, Calendar.APRIL, 1, 2, 8, 7)
        calendar.set(Calendar.MILLISECOND, 6)
        Assertions.assertEquals("1985/04/01:02H08M07S006", calendar.fullString())
    }
}