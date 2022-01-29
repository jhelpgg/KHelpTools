package khelp.musicPlayer

import khelp.musicPlayer.ui.MusicPlayerFrame
import khelp.thread.delay

fun main()
{
    delay(2048) { MusicPlayerFrame.launchPlayer() }.waitCompletion()
}
