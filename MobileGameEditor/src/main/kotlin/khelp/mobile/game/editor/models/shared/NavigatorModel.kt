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

    fun save()

    fun saveAs()

    fun exit()

    fun addNode()

    fun addBox()

    fun addSphere()

    fun addRevolution()

    fun addObject()

    fun projectTextures()

    fun importTexture()

    fun editMaterial()

    fun editTexture()

    fun back() : Boolean
}
