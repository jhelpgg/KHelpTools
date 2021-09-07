package khelp.thread

import java.util.concurrent.Semaphore
import kotlin.math.max

/**
 * Reader/writer problem
 *
 * A reader can read if  their no writer and the number of reader is under the maximum authorized.
 * If is not possible to read now, the calling thread is asleep until reader turn and previous conditions matches.
 *
 * A writer can writer if no reader read and no other writer write.
 * If is not possible to write now, the calling thread is asleep until writer turn and previous conditions matches.
 */
class ReaderWriter(maxNumberOfReaderInSameTime : Int = 16)
{
    private val maxNumberOfReaderInSameTime = max(1, maxNumberOfReaderInSameTime)
    private val freeSpaces = Semaphore(this.maxNumberOfReaderInSameTime, true)

    /**
     * Do action as reader
     *
     * A reader can read if  their no writer and the number of reader is under the maximum authorized.
     * If is not possible to read now, the calling thread is asleep until reader turn and previous conditions matches.
     */
    fun <T> read(reader : () -> T) : T
    {
        var exceptionHappen : Exception? = null
        var result : T? = null

        this.freeSpaces.acquire(1)

        try
        {
            result = reader()
        }
        catch (exception : Exception)
        {
            exceptionHappen = exception
        }

        this.freeSpaces.release(1)

        if (exceptionHappen != null)
        {
            throw exceptionHappen
        }

        return result !!
    }

    /**
     * Do action as writer
     *
     * A writer can writer if no reader read and no other writer write.
     * If is not possible to write now, the calling thread is asleep until writer turn and previous conditions matches.
     */
    fun <T> write(writer : () -> T) : T
    {
        var exceptionHappen : Exception? = null
        var result : T? = null

        this.freeSpaces.acquire(this.maxNumberOfReaderInSameTime)

        try
        {
            result = writer()
        }
        catch (exception : Exception)
        {
            exceptionHappen = exception
        }

        this.freeSpaces.release(this.maxNumberOfReaderInSameTime)

        if (exceptionHappen != null)
        {
            throw exceptionHappen
        }

        return result !!
    }
}
