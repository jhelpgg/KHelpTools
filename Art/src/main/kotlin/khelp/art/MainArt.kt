package khelp.art

import khelp.art.ui.FrameArt
import khelp.io.ClassSource
import khelp.preferences.Preferences
import khelp.resources.Resources

private class MainArt

internal val resourcesArt = Resources(ClassSource(MainArt::class.java))
internal val preferencesArt = Preferences()
internal const val LAST_DIRECTORY_KEY = "lastDirectory"
internal const val ARC_SIZE = 16

fun main()
{
    FrameArt.show()
}