package khelp.utilities.thread

import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MutexTests
{
    @Test
    fun criticalSection()
    {
        val mutex = Mutex()
        val counter = AtomicInteger(0)
        val maxCounter = AtomicInteger(0)
        val threads = ArrayList<Thread>()

        for (count in 1..10)
        {
            val thread = Thread {
                Thread.sleep((1L..32L).random())
                mutex.playInCriticalSectionVoid {
                    Thread.sleep((32L..128L).random())
                    maxCounter.set(max(maxCounter.get(), counter.incrementAndGet()))
                    Thread.sleep((32L..128L).random())
                    counter.decrementAndGet()
                }
            }
            threads += thread
            thread.start()
        }

        for (thread in threads)
        {
            thread.join()
        }

        Assertions.assertEquals(0, counter.get())
        Assertions.assertEquals(1, maxCounter.get())
    }
}