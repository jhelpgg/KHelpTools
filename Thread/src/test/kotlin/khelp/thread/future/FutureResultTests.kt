package khelp.thread.future

import khelp.thread.delay
import khelp.thread.parallel
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FutureResultTests
{
    @Test
    fun joinAllVarargTest()
    {
        var future = FutureResult.joinAll()
        Assertions.assertEquals(FutureResultStatus.SUCCEED, future.status())

        var future1 = parallel {
            var previous = 1L
            var actual = 1L
            var temp: Long

            for (times in 0 until 25)
            {
                temp = actual
                actual += previous
                previous = temp
            }

            actual
        }
        future = FutureResult.joinAll(future1)
        future.waitCompletion()
        Assertions.assertEquals(FutureResultStatus.SUCCEED, future1.status())
        var result1 = 0L
        future1.onResult { result -> result1 = result }
        Assertions.assertEquals(196418L, result1)

        future1 = delay(64) {
            var previous = 1L
            var actual = 1L
            var temp: Long

            for (times in 0 until 25)
            {
                temp = actual
                actual += previous
                previous = temp
            }

            actual
        }
        val future2 = delay(128) {
            "Hello"
        }
        future = FutureResult.joinAll(future1, future2)
        future.waitCompletion()
        Assertions.assertEquals(FutureResultStatus.SUCCEED, future1.status())
        Assertions.assertEquals(FutureResultStatus.SUCCEED, future2.status())
        result1 = 0L
        future1.onResult { result -> result1 = result }
        Assertions.assertEquals(196418L, result1)
        var result2 = ""
        future2.onResult { result -> result2 = result }
        Assertions.assertEquals("Hello", result2)
    }

    @Test
    fun joinAllListTest()
    {
        var future = FutureResult.joinAll(emptyList())
        Assertions.assertEquals(FutureResultStatus.SUCCEED, future.status())

        var future1 = parallel {
            var previous = 1L
            var actual = 1L
            var temp: Long

            for (times in 0 until 25)
            {
                temp = actual
                actual += previous
                previous = temp
            }

            actual
        }

        future = FutureResult.joinAll(listOf(future1))
        future.waitCompletion()
        Assertions.assertEquals(FutureResultStatus.SUCCEED, future1.status())
        var result1 = 0L
        future1.onResult { result -> result1 = result }
        Assertions.assertEquals(196418L, result1)

        future1 = delay(64) {
            var previous = 1L
            var actual = 1L
            var temp: Long

            for (times in 0 until 25)
            {
                temp = actual
                actual += previous
                previous = temp
            }

            actual
        }
        val future2 = delay(128) {
            "Hello"
        }
        future = FutureResult.joinAll(listOf(future1, future2))
        future.waitCompletion()
        Assertions.assertEquals(FutureResultStatus.SUCCEED, future1.status())
        Assertions.assertEquals(FutureResultStatus.SUCCEED, future2.status())
        result1 = 0L
        future1.onResult { result -> result1 = result }
        Assertions.assertEquals(196418L, result1)
        var result2 = ""
        future2.onResult { result -> result2 = result }
        Assertions.assertEquals("Hello", result2)
    }

    @Test
    fun joinTest()
    {
        val future1 = delay(64) {
            var previous = 1L
            var actual = 1L
            var temp: Long

            for (times in 0 until 25)
            {
                temp = actual
                actual += previous
                previous = temp
            }

            actual
        }
        val future2 = delay(128) {
            "Hello"
        }
        val future = FutureResult.join(future1, future2)
        future.waitCompletion()
        var pairFuture: Pair<FutureResult<Long>, FutureResult<String>>? = null
        future.onResult { result -> pairFuture = result }
        Assertions.assertNotNull(pairFuture)
        Assertions.assertSame(future1, pairFuture!!.first)
        Assertions.assertSame(future2, pairFuture!!.second)
        Assertions.assertEquals(FutureResultStatus.SUCCEED, future1.status())
        Assertions.assertEquals(FutureResultStatus.SUCCEED, future2.status())
        var result1 = 0L
        future1.onResult { result -> result1 = result }
        Assertions.assertEquals(196418L, result1)
        var result2 = ""
        future2.onResult { result -> result2 = result }
        Assertions.assertEquals("Hello", result2)
    }

    @Test
    fun combineTest()
    {
        val future1 = delay(64) {
            var previous = 1L
            var actual = 1L
            var temp: Long

            for (times in 0 until 25)
            {
                temp = actual
                actual += previous
                previous = temp
            }

            actual
        }
        val future2 = delay(128) {
            "Hello"
        }
        val future = FutureResult.combine(future1, future2) { number, string -> "$string : $number" }
        future.waitCompletion()
        var combination = ""
        future.onResult { result -> combination = result }
        Assertions.assertEquals("Hello : 196418", combination)
    }
}