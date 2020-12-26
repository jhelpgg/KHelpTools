package khelp.utilities.stream

import khelp.utilities.extensions.utf8
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StringOutputStreamTests
{
    @Test
    fun write()
    {
        val stringOutputStream = StringOutputStream()
        stringOutputStream.write("Hello".utf8)
        stringOutputStream.write(" world!".utf8)
        stringOutputStream.write("\n".utf8)
        stringOutputStream.write("This is a test.".utf8)
        stringOutputStream.flush()
        stringOutputStream.close()
        Assertions.assertEquals("Hello world!\nThis is a test.", stringOutputStream.string)
    }
}