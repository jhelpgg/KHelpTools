package khelp.thread.extensions

import khelp.thread.future.FutureResultStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AnyExtensionsTests
{
    @Test
    fun futureResultTest()
    {
        val future = "Hello".futureResult
        Assertions.assertEquals(FutureResultStatus.SUCCEED, future.status())
        var resultRead = ""
        future.onResult { result -> resultRead = result }
        Assertions.assertEquals("Hello", resultRead)
    }
}