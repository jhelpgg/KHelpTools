package khelp.mobile.game.editor

import khelp.io.ClassSource
import khelp.io.obtainExternalFile
import khelp.mobile.game.editor.models.initializeModels
import khelp.mobile.game.editor.ui.MobileGameEditor
import khelp.resources.Resources
import khelp.ui.game.GameImage

private class MainMobileGameEditor

val EDITOR_RESOURCES = Resources(ClassSource(MainMobileGameEditor::class.java))
val EDITOR_TEXTS = EDITOR_RESOURCES.resourcesText("texts/editorTexts")
val MAIN_DIRECTORY = obtainExternalFile("data")
const val MAIN_NODE = "MainNode"
fun editorImage(name : String) : GameImage = GameImage.load("images/$name", EDITOR_RESOURCES)


fun main()
{
    initializeModels()
    MobileGameEditor.show()
}
