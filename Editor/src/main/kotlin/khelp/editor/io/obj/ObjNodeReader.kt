package khelp.editor.io.obj

import khelp.editor.io.NodeReader
import khelp.engine3d.render.Node
import java.io.IOException
import java.io.InputStream

object ObjNodeReader : NodeReader()
{

    @Throws(IOException::class)
    override fun readNode(inputStream : InputStream) : Node
    {
        TODO("Not yet implemented")
    }
}