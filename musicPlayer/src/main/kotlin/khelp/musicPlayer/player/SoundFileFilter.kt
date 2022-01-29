package khelp.musicPlayer.player

import khelp.io.extensions.isVirtualLink
import java.io.File
import java.io.FileFilter

object SoundFileFilter : FileFilter
{
    private val SOUND_EXTENSIONS = arrayOf("mp3", "mid", "midi", "wav", "au")

    override fun accept(pathname : File) : Boolean
    {
        return pathname.exists() && pathname.canRead() && ! pathname.isVirtualLink && (pathname.isDirectory || pathname.extension.lowercase() in SOUND_EXTENSIONS)
    }
}