package khelp.preferences

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PreferencesTests
{
    @Test
    fun preferencesReadWrite()
    {
        val preferences = Preferences()
        preferences["boolean"] = true
        preferences["int"] = 42
        preferences["long"] = 666L
        preferences["float"] = 42.73f
        preferences["double"] = 77.77
        preferences["numberType"] = NumberType.FLOAT

        Assertions.assertEquals(true, preferences["boolean", false])
        Assertions.assertEquals(42, preferences["int", 0])
        Assertions.assertEquals(666L, preferences["long", 0L])
        Assertions.assertEquals(42.73f, preferences["float", 0f], 0.01f)
        Assertions.assertEquals(77.77, preferences["double", 0.0], 0.01)
        Assertions.assertEquals(NumberType.FLOAT, preferences["numberType", NumberType.BYTE])
    }
}