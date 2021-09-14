package khelp.sound.mp3

import khelp.sound.SoundProgress
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.utilities.extensions.bounds
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * Input stream we can control the reading
 */
internal class ControlInputStream(private val file : File) : InputStream()
{
    var pause = false
    val size = file.length()

    private val maximum = this.size - 1L
    private var mark = 0L
    private var randomAccessFile = RandomAccessFile(this.file, "r")
    private var fileChanel = this.randomAccessFile.channel
    private val progressObservableData = ObservableData<SoundProgress>(SoundProgress(0L, this.maximum))

    val progressObservable : Observable<SoundProgress> = this.progressObservableData.observable
    var position : Long
        get() = this.fileChanel.position()
        set(value)
        {
            val position = value.bounds(0L, this.maximum)
            this.fileChanel.position(position)
            this.progressObservableData.value(SoundProgress(position, this.maximum))
        }

    /**
     * Reads the next byte of data from the input stream. The value byte is
     * returned as an `int` in the range `0` to
     * `255`. If no byte is available because the end of the stream
     * has been reached, the value `-1` is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     *
     *
     *  A subclass must provide an implementation of this method.
     *
     * @return     the next byte of data, or `-1` if the end of the
     * stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
    override fun read() : Int
    {
        if (this.pause)
        {
            return - 1
        }

        val read = this.randomAccessFile.read()
        this.progressObservableData.value(SoundProgress(this.fileChanel.position(), this.maximum))
        return read
    }

    /**
     * Read several bytes
     *
     * @param b Array to fill
     * @return Number of bytes read
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream.read
     */
    override fun read(b : ByteArray) : Int
    {
        if (this.pause)
        {
            return - 1
        }

        val read = this.randomAccessFile.read(b)
        this.progressObservableData.value(SoundProgress(this.fileChanel.position(), this.maximum))
        return read
    }

    /**
     * Read several bytes
     *
     * @param b   Array to fill
     * @param off Where start to fill
     * @param len Number of desired bytes
     * @return Number of bytes read
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream.read
     */
    override fun read(b : ByteArray, off : Int, len : Int) : Int
    {
        if (this.pause)
        {
            return - 1
        }

        val read = this.randomAccessFile.read(b, off, len)
        this.progressObservableData.value(SoundProgress(this.fileChanel.position(), this.maximum))
        return read
    }

    /**
     * Skip number of bytes
     *
     * @param n Number of bytes to skip
     * @return Number of bytes really skipped
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream.skip
     */
    override fun skip(n : Long) : Long
    {
        val skipped = this.randomAccessFile.skipBytes(n.toInt())
            .toLong()
        this.progressObservableData.value(SoundProgress(this.fileChanel.position(), this.maximum))
        return skipped
    }

    /**
     * Returns the number of bytes that can be read
     *
     * @return Data size
     * @see InputStream.available
     */
    override fun available() : Int = (this.size - this.fileChanel.position()).toInt()

    /**
     * Mark the actual position
     *
     * @param readlimit Limit to keep
     * @see InputStream.mark
     */
    @Synchronized
    override fun mark(readlimit : Int)
    {
        this.mark = this.position
    }

    /**
     * Reset to last mark
     *
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream.reset
     */
    @Synchronized
    override fun reset()
    {
        this.position = this.mark
    }

    /**
     * Indicates that mark are supported
     *
     * @return `true`
     * @see InputStream.markSupported
     */
    override fun markSupported() = true

    fun resetAt0()
    {
        this.pause = false
        this.randomAccessFile = RandomAccessFile(this.file, "r")
        this.fileChanel = this.randomAccessFile.channel
        this.position = 0
    }

    override fun close()
    {
        this.fileChanel.close()
        this.randomAccessFile.close()
    }
}
