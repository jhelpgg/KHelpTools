package khelp.mobile.game.editor.ui.screens

import khelp.engine3d.render.Window3D

interface Screen
{
    fun attach(window3D : Window3D)

    fun detach()
}
