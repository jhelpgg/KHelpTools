package khelp.thread

import khelp.thread.future.FutureResult
import khelp.utilities.log.verbose
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Vector
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

class ReaderWriterTests
{
    @Test
    fun readWrite()
    {
        val readerCount = AtomicInteger(0)
        val maxReaderCount = AtomicInteger(0)
        val writerCount = AtomicInteger(0)
        val maxWriterCount = AtomicInteger(0)
        val readerWriter = ReaderWriter(4)
        val vector = Vector<Boolean>()

        val reader = {
            readerWriter.read {
                vector.add(true)
                val number = readerCount.incrementAndGet()
                maxReaderCount.set(max(maxReaderCount.get(), number))
                Thread.sleep(64)
                readerCount.decrementAndGet()
            }

            Unit
        }

        val writer = {
            readerWriter.write {
                vector.add(false)
                val number = writerCount.incrementAndGet()
                maxWriterCount.set(max(maxWriterCount.get(), number))
                Thread.sleep(64)
                writerCount.decrementAndGet()
            }

            Unit
        }

        val futures = ArrayList<FutureResult<Unit>>()

        for (count in 0 until 16)
        {
            futures.add(parallel { writer() })

            for (countR in 0 until 8)
            {
                futures.add(parallel { reader() })
            }

            futures.add(parallel { writer() })
        }

        FutureResult.joinAll(futures)
            .waitCompletion()
        Assertions.assertEquals(1, maxWriterCount.get())
        Assertions.assertTrue(maxReaderCount.get() <= 4, "Over reader : ${maxReaderCount.get()}")
        verbose("Number reader = ${maxReaderCount.get()}")
        print(vector)
    }

    private fun print(vector : Vector<Boolean>) {
        var value = vector[0]
        var count = 1

        for(index in 1 until vector.size)
        {
            val next = vector[index]

            if(next == value)
            {
                count ++
            }
            else
            {
                verbose(count, ") ", value)
                value = next
                count = 1
            }
        }

        verbose(count, ") ", value)
    }
}
