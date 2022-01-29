package khelp.musicPlayer.player

import java.io.File
import java.io.FileFilter

object MediaFilter : FileFilter
{
    override fun accept(pathname : File) : Boolean =
        ImageFileFilter.accept(pathname) || SoundFileFilter.accept(pathname)
}
