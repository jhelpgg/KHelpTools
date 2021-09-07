package khelp.thread

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

class LockerTests
{
    @Test
    fun lockUnlock()
    {
        val blocked = AtomicBoolean(false)
        val locker = Locker()
        delay(1024) {
            blocked.set(true)
            locker.unlock()
        }
        locker.unlock()
        locker.lock()
        Assertions.assertFalse(blocked.get(), "Should not be blocked since unlock is before lock")
        locker.lock()
        Assertions.assertTrue(blocked.get(), "Should be blocked since unlock is after lock")
    }
}
