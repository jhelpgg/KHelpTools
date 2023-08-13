package khelp.uno.game

import khelp.engine3d.event.MouseManager3D
import khelp.engine3d.render.Scene
import khelp.engine3d.render.Window3D
import khelp.thread.future.FutureResult

interface GameScreen
{
    fun play(window3D : Window3D) : FutureResult<GameAction>
}
