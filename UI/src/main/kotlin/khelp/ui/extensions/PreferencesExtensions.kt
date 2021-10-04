package khelp.ui.extensions

import khelp.preferences.Preferences
import khelp.preferences.PreferencesEditor
import khelp.ui.font.JHelpFont
import java.awt.Color
import java.io.File

operator fun Preferences.get(key : String, defaultValue : Color) : Color =
    Color(this[key, defaultValue.rgb], true)

operator fun Preferences.get(key : String, defaultValue : JHelpFont) : JHelpFont
{
    val family = this["$key.family", defaultValue.family]
    val size = this["$key.size", defaultValue.size]
    val bold = this["$key.bold", defaultValue.bold]
    val italic = this["$key.italic", defaultValue.italic]
    val underline = this["$key.underline", defaultValue.underline]
    return JHelpFont(family, size, bold, italic, underline)
}

operator fun Preferences.get(key : String, defaultValue : File) : File =
    File(this[key, defaultValue.absolutePath])

fun PreferencesEditor.color(key : String, value : Color)
{
    key IS value.rgb
}

fun PreferencesEditor.font(key : String, value : JHelpFont)
{
    "$key.family" IS value.family
    "$key.size" IS value.size
    "$key.bold" IS value.bold
    "$key.italic" IS value.italic
    "$key.underline" IS value.underline
}

fun PreferencesEditor.file(key : String, file : File)
{
    key IS file.absolutePath
}
