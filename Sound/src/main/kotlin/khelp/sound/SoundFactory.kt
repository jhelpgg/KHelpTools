package khelp.sound

import khelp.io.extensions.createDirectory
import khelp.io.outsideDirectory
import khelp.io.write
import khelp.resources.Resources
import khelp.sound.midi.SoundMidi
import khelp.sound.mp3.SoundMP3
import khelp.sound.other.SoundOther
import khelp.utilities.log.debug
import java.io.File
import java.io.InputStream
import java.net.URL

val DIRECTORY_SOUNDS : File by lazy {
    val directory = File(outsideDirectory, "media/sounds/")
    directory.createDirectory()
    directory
}

fun soundFromFile(file : File) : Sound
{
    val sound =
        when (file.extension.toLowerCase())
        {
            "mp3"  -> SoundMP3(file)
            "mid"  -> SoundMidi(file)
            "midi" -> SoundMidi(file)
            else   -> SoundOther(file)
        }

    return Sound(sound)
}

fun soundFromResource(resourceName : String, resources : Resources) : Sound =
    soundFromStream({ resources.inputStream(resourceName) }, resourceName)

fun soundFromURL(url : URL) : Sound
{
    val name = url.toString()
    val fileName = name.replace("://", "/")
        .replace(":/", "/")
        .replace(":", "/")
        .replace("?", "/")
        .replace("&", "/")
        .replace("=", "/")
    return soundFromStream({ url.openStream() }, fileName)
}

private fun soundFromStream(streamProducer : () -> InputStream, fileName : String) : Sound
{
    val destination = File(DIRECTORY_SOUNDS, fileName)

    if (! destination.exists())
    {
        val stream = streamProducer()
        write(stream, destination)
        stream.close()
    }

    return soundFromFile(destination)
}
