package khelp.thread

import java.util.concurrent.CancellationException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import khelp.thread.future.FutureResultStatus
import org.junit.Assert
import org.junit.Test

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

        Assert.assertFalse("complete", complete.get())
        Assert.assertEquals(Int.MIN_VALUE, resultReach.get())
        Assert.assertNull("Failure", failReach.get())
        val cancelException = cancelReach.get()
        Assert.assertNotNull("Cancel", cancelException)
        Assert.assertEquals("Test", cancelException?.message)
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

        Assert.assertFalse("complete", complete.get())
        Assert.assertEquals(Int.MIN_VALUE, resultReach.get())
        val throwable = failReach.get()
        Assert.assertNotNull("Failure", throwable)
        Assert.assertTrue("Should be arithmetic exception", throwable is ArithmeticException)
        Assert.assertNull("Cancel", cancelReach.get())
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

        Assert.assertTrue("complete", complete.get())
        Assert.assertEquals(42, resultReach.get())
        Assert.assertNull("Failure", failReach.get())
        Assert.assertNull("Cancel", cancelReach.get())
    }

    @Test
    fun delayedCancelAnd()
    {
        val futureSource = delay(1024) { 21 }
        val future = futureSource.and { 2 * it }
        Thread.sleep(512)
        future.cancel("Test")
        future.waitCompletion()
        Assert.assertEquals(FutureResultStatus.CANCELED, futureSource.status())
        Assert.assertEquals(FutureResultStatus.CANCELED, future.status())
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
        Assert.assertEquals(FutureResultStatus.SUCCEED, futureSource.status())
        Assert.assertEquals(FutureResultStatus.CANCELED, future.status())
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
        Assert.assertEquals(73, result.get())
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
        Assert.assertEquals(-987, result.get())
    }
}