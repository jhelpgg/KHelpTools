package khelp.utilities.thread

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AtomicsTests
{
    var boolean by atomic(false)
    var integer by atomic(42)
    var string by atomic("Test")

    @Test
    fun booleanAtomic()
    {
        Assertions.assertFalse(this.boolean)
        this.boolean = true
        Assertions.assertTrue(this.boolean)
    }

    @Test
    fun intAtomic()
    {
        Assertions.assertEquals(42, this.integer)
        this.integer = 73
        Assertions.assertEquals(73, this.integer)
    }

    @Test
    fun stringAtomic()
    {
        Assertions.assertEquals("Test", this.string)
        this.string = "Something"
        Assertions.assertEquals("Something", this.string)
    }
}