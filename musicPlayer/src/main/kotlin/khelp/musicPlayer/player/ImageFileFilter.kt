package khelp.musicPlayer.player

import khelp.io.extensions.isVirtualLink
import java.io.File
import java.io.FileFilter

object ImageFileFilter : FileFilter
{
    private val IMAGE_EXTENSIONS = arrayOf("png", "jpg")

    override fun accept(pathname : File) : Boolean
    {
        return pathname.exists() && pathname.canRead() && ! pathname.isVirtualLink && (pathname.isDirectory || pathname.extension.lowercase() in IMAGE_EXTENSIONS)
    }
}