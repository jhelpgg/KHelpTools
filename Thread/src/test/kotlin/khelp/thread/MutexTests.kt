package khelp.thread

import khelp.thread.future.FutureResult
import khelp.utilities.log.debug
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

class MutexTests
{
    @Test
    fun criticalSection()
    {
        val numberInSameTime = AtomicInteger(0)
        val maxNumberInSameTime = AtomicInteger(0)
        val mutex = Mutex()
        val futures = ArrayList<FutureResult<Unit>>()

        for (count in 0 until 16)
        {
            futures.add(parallel {
                mutex {
                    val number = numberInSameTime.incrementAndGet()
                    maxNumberInSameTime.getAndUpdate { value -> max(value, number) }
                    Thread.sleep(128)
                    numberInSameTime.decrementAndGet()
                }

                Unit
            })
        }

        FutureResult.joinAll(futures)
            .waitCompletion()
        Assertions.assertEquals(1, maxNumberInSameTime.get())
    }
}
