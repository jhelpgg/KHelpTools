package khelp.uno

import khelp.io.ClassSource
import khelp.preferences.Preferences
import khelp.resources.Resources
import khelp.uno.ui.Uno

private class MainUno

internal val resourcesUno = Resources(ClassSource(MainUno::class.java))
internal val resourcesTextUno = resourcesUno.resourcesText(path = "texts/texts")
internal val preferencesUno = Preferences()

fun main()
{
    Uno.show()
}
