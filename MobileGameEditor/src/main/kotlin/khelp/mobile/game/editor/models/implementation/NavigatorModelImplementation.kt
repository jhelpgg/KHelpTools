package khelp.mobile.game.editor.models.implementation

import khelp.io.DirectorySource
import khelp.mobile.game.editor.MAIN_DIRECTORY
import khelp.mobile.game.editor.models.shared.NavigatorModel
import khelp.mobile.game.editor.models.shared.Screens
import khelp.resources.Resources
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import java.io.File

internal class NavigatorModelImplementation : NavigatorModel
{
    private val screensObservableData = ObservableData<Screens>(Screens.MAIN_SCREEN)
    override val screen : Observable<Screens> = this.screensObservableData.observable
    private var projectDirectory = MAIN_DIRECTORY
    private var projectResources = Resources(DirectorySource(MAIN_DIRECTORY))

    override fun projectDirectory() : File = this.projectDirectory

    override fun projectResources() : Resources = this.projectResources

    override fun newProject()
    {
        //  TODO("Not yet implemented")
    }

    override fun openProject()
    {
        // TODO("Not yet implemented")
    }

    override fun back() : Boolean
    {
        //TODO("Not yet implemented")
        return true
    }
}
