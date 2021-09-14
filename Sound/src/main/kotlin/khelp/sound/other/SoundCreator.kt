package khelp.sound.other

import khelp.sound.SoundException
import java.io.File
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine.Info

internal fun createSound(file : File) : Pair<AudioInputStream, Clip>
{
    var audioInputStream : AudioInputStream? = null
    var clip : Clip? = null

    try
    {
        audioInputStream = AudioSystem.getAudioInputStream(file)
        var audioFormat = audioInputStream.format

        if (audioFormat.encoding === AudioFormat.Encoding.ULAW || audioFormat.encoding === AudioFormat.Encoding.ALAW)
        {
            // Create new format
            val tmp = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.sampleRate,
                                  audioFormat.sampleSizeInBits * 2,
                                  audioFormat.channels, audioFormat.frameSize * 2,
                                  audioFormat.frameRate, true)

            // Force the stream be the new format
            audioInputStream = AudioSystem.getAudioInputStream(tmp, audioInputStream)
            audioFormat = tmp
        }

        // Get sound information
        val info = Info(Clip::class.java, audioFormat,
                        audioInputStream.frameLength.toInt() * audioFormat.frameSize)

        if (! AudioSystem.isLineSupported(info))
        {
            throw SoundException("Info is not supported !")
        }

        // Create clip for play sound
        clip = AudioSystem.getLine(info) as Clip

        // Link the clip to the sound
        clip.open(audioInputStream)

        return Pair(audioInputStream, clip)
    }
    catch (exception : Exception)
    {
        if (clip != null)
        {
            try
            {
                clip.flush()
                clip.close()
            }
            catch (ignored : Exception)
            {
            }
        }

        if (audioInputStream != null)
        {
            try
            {
                audioInputStream.close()
            }
            catch (ignored : Exception)
            {
            }
        }

        throw SoundException("Failed to create sound from: ${file.absolutePath}", exception)
    }
}
