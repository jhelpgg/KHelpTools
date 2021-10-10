package khelp.editor.io

import khelp.engine3d.render.Node
import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.parallel
import java.io.IOException
import java.io.InputStream

abstract class NodeReader
{
    fun readNode(streamProducer : () -> InputStream) : FutureResult<Node>
    {
        return parallel(TaskContext.IO) {
            var inputStream : InputStream? = null

            try
            {
                inputStream = streamProducer()
                this.readNode(inputStream)
            }
            finally
            {
                if (inputStream != null)
                {
                    try
                    {
                        inputStream.close()
                    }
                    catch (_ : Exception)
                    {
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    protected abstract fun readNode(inputStream : InputStream) : Node
}