package khelp.engine3d.sound3d

import khelp.resources.Resources
import khelp.utilities.log.exception
import java.io.InputStream

fun createSoundMP3(path : String, resources : Resources) : Sound =
    try
    {
        createSoundMP3(resources.inputStream(path))
    }
    catch (exception : Exception)
    {
        exception(exception, "path=", path)
        DummySound
    }

fun createSoundMP3(inputStream : InputStream) : Sound =
    try
    {
        SoundMP3(inputStream)
    }
    catch (exception : Exception)
    {
        exception(exception)
        DummySound
    }

fun createSoundWav(path : String, resources : Resources) : Sound =
    try
    {
        createSoundWav(resources.inputStream(path))
    }
    catch (exception : Exception)
    {
        exception(exception, "path=", path)
        DummySound
    }

fun createSoundWav(inputStream : InputStream) : Sound =
    try
    {
        SoundWav.create(inputStream)
    }
    catch (exception : Exception)
    {
        exception(exception)
        DummySound
    }
