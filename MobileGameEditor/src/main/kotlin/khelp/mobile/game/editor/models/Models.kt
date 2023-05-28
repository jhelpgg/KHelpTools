package khelp.mobile.game.editor.models

import khelp.mobile.game.editor.models.implementation.NavigatorModelImplementation
import khelp.mobile.game.editor.models.shared.NavigatorModel
import khelp.utilities.provider.provideSingle

fun initializeModels()
{
    provideSingle<NavigatorModel> { NavigatorModelImplementation() }
}
