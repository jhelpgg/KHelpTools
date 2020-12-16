package khelp.utilities.stream

import khelp.utilities.extensions.utf8
import org.junit.Assert
import org.junit.Test

class StringInputStreamTests
{
    @Test
    fun read()
    {
        val stringInputStream = StringInputStream("Hello world!\nThis is a test.")
        val bytes = ArrayList<Byte>()
        val buffer = ByteArray(4096)
        var read = stringInputStream.read(buffer)

        while (read >= 0)
        {
            for (index in 0 until read)
            {
                bytes.add(buffer[index])
            }

            read = stringInputStream.read(buffer)
        }

        stringInputStream.close()
        val data = ByteArray(bytes.size) { index -> bytes[index] }
        Assert.assertEquals("Hello world!\nThis is a test.", data.utf8)
    }
}