package khelp.thread.weak

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class InstanceTimeoutTests
{
    @Test
    fun instanceLostIfTimeoutExpired()
    {
        val instanceTimeout = InstanceTimeout(Something(), 256)
        Thread.sleep(512)
        Assertions.assertNull(instanceTimeout())
    }

    @Test
    fun reinitializeDelayIfRequestBeforeTimeoutExpires()
    {
        val instanceTimeout = InstanceTimeout(Something(), 512)
        Thread.sleep(256)
        Assertions.assertNotNull(instanceTimeout())
        Thread.sleep(256)
        Assertions.assertNotNull(instanceTimeout())
        Thread.sleep(256)
        Assertions.assertNotNull(instanceTimeout())
        Thread.sleep(1024)
        Assertions.assertNull(instanceTimeout())
    }
}