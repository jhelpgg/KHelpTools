package khelp.utilities.weak

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ByWeakTests
{
    private val initial = Reference("INITIAL")
    private var referenceNotInitialized : Reference? by weak()
    private var referenceInitialized : Reference? by weak(this.initial)

    @Test
    fun notInitializedTest()
    {
        Assertions.assertNull(this.referenceNotInitialized)
        this.referenceNotInitialized = Reference("Test")
        this.garbage()
        Assertions.assertNull(this.referenceNotInitialized)
        var test : Reference? = Reference("Test")
        this.referenceNotInitialized = test
        Assertions.assertEquals(test, this.referenceNotInitialized)
        test = null
        this.garbage()
        Assertions.assertNull(this.referenceNotInitialized)
        println("test=$test")
    }

    @Test
    fun initialized()
    {
        Assertions.assertEquals(this.initial, this.referenceInitialized)
        this.referenceInitialized = Reference("Test")
        this.garbage()
        Assertions.assertNull(this.referenceInitialized)
        var test : Reference? = Reference("Test")
        this.referenceInitialized = test
        Assertions.assertEquals(test, this.referenceInitialized)
        test = null
        this.garbage()
        Assertions.assertNull(this.referenceInitialized)
        println("test=$test")
    }

    private fun garbage() {
        System.gc()
        Thread.sleep(1024)
        System.gc()
        Thread.sleep(1024)
        System.gc()
    }
}