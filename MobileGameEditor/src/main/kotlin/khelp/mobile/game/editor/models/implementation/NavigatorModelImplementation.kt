package khelp.mobile.game.editor.models.implementation

import khelp.engine3d.gui.frames.FrameFileChooser
import khelp.engine3d.gui.frames.FrameInputText
import khelp.engine3d.render.Window3D
import khelp.io.DirectorySource
import khelp.mobile.game.editor.EDITOR_TEXTS
import khelp.mobile.game.editor.MAIN_DIRECTORY
import khelp.mobile.game.editor.models.shared.NavigatorModel
import khelp.mobile.game.editor.models.shared.Screens
import khelp.resources.Resources
import khelp.resources.standardText
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.ui.components.message.MessageAction
import khelp.ui.components.message.MessageButtons
import khelp.ui.components.message.MessageType
import khelp.utilities.log.debug
import java.io.File

internal class NavigatorModelImplementation : NavigatorModel
{
    private val screensObservableData = ObservableData<Screens>(Screens.MAIN_SCREEN)
    override val screen : Observable<Screens> = this.screensObservableData.observable
    private var projectDirectory = MAIN_DIRECTORY
    private var projectResources = Resources(DirectorySource(MAIN_DIRECTORY))
    private var window3D : Window3D? = null

    override fun window3D(window3D : Window3D)
    {
        this.window3D = window3D
    }

    override fun projectDirectory() : File = this.projectDirectory

    override fun projectResources() : Resources = this.projectResources

    override fun newProject()
    {
        //  TODO("Not yet implemented")
    }

    override fun openProject()
    {
        FrameFileChooser.show()
            .and { file -> debug(file.absolutePath) }
            .onCancel { reason -> debug("Cancel", reason) }
        // TODO("Not yet implemented")
    }

    override fun save()
    {
        //TODO("Not yet implemented")
    }

    override fun saveAs()
    {
        //  TODO("Not yet implemented")
    }

    override fun exit()
    {
        if (this.confirmExit())
        {
            this.window3D?.close()
        }
    }

    override fun addNode()
    {
        FrameInputText.show(titleKeyText = "Type some text".standardText)
            .and { text -> debug(text) }
            .onCancel { reason -> debug("Cancel", reason) }
        //   TODO("Not yet implemented")
    }

    override fun addBox()
    {
        //   TODO("Not yet implemented")
    }

    override fun addSphere()
    {
        // TODO("Not yet implemented")
    }

    override fun addRevolution()
    {
        // TODO("Not yet implemented")
    }

    override fun addObject()
    {
        // TODO("Not yet implemented")
    }

    override fun projectTextures()
    {
        //  TODO("Not yet implemented")
    }

    override fun importTexture()
    {
        //  TODO("Not yet implemented")
    }

    override fun editMaterial()
    {
        //  TODO("Not yet implemented")
    }

    override fun editTexture()
    {
        //TODO("Not yet implemented")
    }

    /**
     * @return `true` when have to exit the application
     */
    override fun back() : Boolean
    {
        //TODO("Not yet implemented")
        return this.confirmExit()
    }

    private fun confirmExit() : Boolean
    {
        val window3D = this.window3D ?: return true

        val future = window3D.gui.dialogMessage.show(MessageType.QUESTION, MessageButtons.YES_NO,
                                                     "exitConfirmation", EDITOR_TEXTS)
        future.waitCompletion()

        return future.result() == MessageAction.YES
    }
}
