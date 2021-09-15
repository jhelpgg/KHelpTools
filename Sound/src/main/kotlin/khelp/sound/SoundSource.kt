package khelp.sound

import khelp.resources.Resources
import java.io.File
import java.net.URL

sealed class SoundSource
{
    abstract val sound : Sound
}

class SoundFromFile(private val file : File) : SoundSource()
{
    override val sound : Sound get() = soundFromFile(this.file)
}

class SoundFromResources(private val name : String, private val resources : Resources) : SoundSource()
{
    override val sound : Sound get() = soundFromResource(this.name, this.resources)
}

class SoundFromUrl(private val url : URL) : SoundSource()
{
    override val sound : Sound get() = soundFromURL(this.url)
}
