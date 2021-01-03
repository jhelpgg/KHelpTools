package khelp.thread.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FutureResultExtensionsTests
{
    @Test
    fun unwrapTest()
    {
        val future = "Hello".futureResult.futureResult.unwrap()
        future.waitCompletion()
        var resultRead = ""
        future.onResult { result -> resultRead = result }
        Assertions.assertEquals("Hello", resultRead)
    }
}