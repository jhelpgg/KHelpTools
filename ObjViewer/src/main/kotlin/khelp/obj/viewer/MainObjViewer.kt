package khelp.obj.viewer

import khelp.obj.viewer.ui.FrameObjViewer
import java.io.File

fun main(args : Array<String>)
{
    var fileObj : File? = null

    if (args.isNotEmpty())
    {
        val file = File(args[0])

        if (file.exists() && file.isFile)
        {
            fileObj = file
        }
    }

    FrameObjViewer.show(fileObj)
}
