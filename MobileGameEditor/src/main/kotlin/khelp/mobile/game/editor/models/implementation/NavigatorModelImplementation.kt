package khelp.mobile.game.editor.models.implementation

import khelp.mobile.game.editor.models.shared.NavigatorModel
import khelp.mobile.game.editor.models.shared.Screens
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData

internal class NavigatorModelImplementation : NavigatorModel
{
    private val screensObservableData = ObservableData<Screens>(Screens.MAIN_SCREEN)
    override val screen : Observable<Screens> = this.screensObservableData.observable

    override fun back() : Boolean
    {
        //TODO("Not yet implemented")
        return true
    }
}
