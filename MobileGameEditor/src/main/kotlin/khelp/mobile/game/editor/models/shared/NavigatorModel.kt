package khelp.mobile.game.editor.models.shared

import khelp.resources.Resources
import khelp.thread.observable.Observable
import java.io.File

interface NavigatorModel
{
    val screen : Observable<Screens>

    fun projectDirectory() : File

    fun projectResources() : Resources

    fun newProject()

    fun openProject()

    fun back() : Boolean
}
