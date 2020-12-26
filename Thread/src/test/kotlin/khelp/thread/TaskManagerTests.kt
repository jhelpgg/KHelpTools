package khelp.thread

import java.util.concurrent.CancellationException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import khelp.thread.future.FutureResultStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TaskManagerTests
{
    @Test
    fun delayCancelTest()
    {
        val complete = AtomicBoolean(false)
        val resultReach = AtomicInteger(Int.MIN_VALUE)
        val failReach = AtomicReference<Throwable>(null)
        val cancelReach = AtomicReference<CancellationException>(null)

        val future = delay(1024) {
            var x = 0
            x++
            x--
            val y = 1 / x
            complete.set(true)
            y
        }
        future.onResult { resultReach.set(it) }
        future.onFailure { failReach.set(it) }
        future.onCancel { cancelReach.set(it) }

        Thread.sleep(512)
        future.cancel("Test")

        future.waitCompletion()

        Assertions.assertFalse(complete.get(), "complete")
        Assertions.assertEquals(Int.MIN_VALUE, resultReach.get())
        Assertions.assertNull(failReach.get(), "Failure")
        val cancelException = cancelReach.get()
        Assertions.assertNotNull(cancelException, "Cancel")
        Assertions.assertEquals("Test", cancelException?.message)
    }

    @Test
    fun delayFailureTest()
    {
        val complete = AtomicBoolean(false)
        val resultReach = AtomicInteger(Int.MIN_VALUE)
        val failReach = AtomicReference<Throwable>(null)
        val cancelReach = AtomicReference<CancellationException>(null)

        val future = delay(1024) {
            var x = 0
            x++
            x--
            val y = 1 / x
            complete.set(true)
            y
        }
        future.onResult { resultReach.set(it) }
        future.onFailure { failReach.set(it) }
        future.onCancel { cancelReach.set(it) }

        future.waitCompletion()

        Assertions.assertFalse(complete.get(), "complete")
        Assertions.assertEquals(Int.MIN_VALUE, resultReach.get())
        val throwable = failReach.get()
        Assertions.assertNotNull(throwable, "Failure")
        Assertions.assertTrue(throwable is ArithmeticException, "Should be arithmetic exception")
        Assertions.assertNull(cancelReach.get(), "Cancel")
    }

    @Test
    fun delaySuccedTest()
    {
        val complete = AtomicBoolean(false)
        val resultReach = AtomicInteger(Int.MIN_VALUE)
        val failReach = AtomicReference<Throwable>(null)
        val cancelReach = AtomicReference<CancellationException>(null)

        val future = delay(1024) {
            var x = 2
            x++
            x--
            val y = 84 / x
            complete.set(true)
            y
        }
        future.onResult { resultReach.set(it) }
        future.onFailure { failReach.set(it) }
        future.onCancel { cancelReach.set(it) }

        future.waitCompletion()

        Assertions.assertTrue(complete.get(), "complete")
        Assertions.assertEquals(42, resultReach.get())
        Assertions.assertNull(failReach.get(), "Failure")
        Assertions.assertNull(cancelReach.get(), "Cancel")
    }

    @Test
    fun delayedCancelAnd()
    {
        val futureSource = delay(1024) { 21 }
        val future = futureSource.and { 2 * it }
        Thread.sleep(512)
        future.cancel("Test")
        future.waitCompletion()
        Assertions.assertEquals(FutureResultStatus.CANCELED, futureSource.status())
        Assertions.assertEquals(FutureResultStatus.CANCELED, future.status())
    }

    @Test
    fun delayedAndWaitCancel()
    {
        val futureSource = delay(64) { 21 }
        val future = futureSource.and {
            Thread.sleep(1024)
            2 * it
        }
        Thread.sleep(512)
        future.cancel("Test")
        future.waitCompletion()
        // Success because had time to execute
        Assertions.assertEquals(FutureResultStatus.SUCCEED, futureSource.status())
        Assertions.assertEquals(FutureResultStatus.CANCELED, future.status())
    }

    @Test
    fun andChainTest()
    {
        val result = AtomicInteger(Int.MIN_VALUE)
        val future = parallel { 3 }.and { it * 7 }
            .and { it * 2 }
            .and { it + 31 }
        future.onResult { result.set(it) }
        future.waitCompletion()
        Assertions.assertEquals(73, result.get())
    }

    @Test
    fun andAndFailThen()
    {
        val result = AtomicInteger(Int.MIN_VALUE)
        val future = parallel { 0 }.and { 5 / it }
            .then { fut ->
                when (fut.status())
                {
                    FutureResultStatus.SUCCEED  -> fut.onResult { result.set(it) }
                    FutureResultStatus.CANCELED -> result.set(-123)
                    FutureResultStatus.FAILED   -> result.set(-987)
                    else                        -> Unit
                }
            }
        future.waitCompletion()
        Assertions.assertEquals(-987, result.get())
    }
}