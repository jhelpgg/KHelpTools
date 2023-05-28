package khelp.mobile.game.editor.models.shared

import khelp.thread.observable.Observable

interface NavigatorModel
{
    val screen : Observable<Screens>

    fun back() : Boolean
}
